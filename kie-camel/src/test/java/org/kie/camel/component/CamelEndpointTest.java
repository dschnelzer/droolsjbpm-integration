/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.kie.camel.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Context;

import org.apache.camel.builder.RouteBuilder;
import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.GetObjectCommand;
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.drools.core.common.DefaultFactHandle;
import org.kie.api.runtime.KieSession;
import org.kie.pipeline.camel.Person;
import org.junit.Test;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.internal.command.CommandFactory;
import org.kie.api.runtime.ExecutionResults;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.api.runtime.rule.FactHandle;

public class CamelEndpointTest extends KieCamelTestSupport {
    private String handle;

    @Test
    public void testSessionInsert() throws Exception {
        Person person = new Person();
        person.setName( "Mauricio" );

        InsertObjectCommand cmd = (InsertObjectCommand) CommandFactory.newInsert( person,
                                                                                  "salaboy" );

        ExecutionResults response = (ExecutionResults) template.requestBody( "direct:test-with-session",
                                                                             cmd );
        assertTrue( "Expected valid ExecutionResults object",
                    response != null );
        assertTrue( "ExecutionResults missing expected fact",
                    response.getFactHandle( "salaboy" ) != null );
        assertTrue( "ExecutionResults missing expected fact",
                    response.getFactHandle( "salaboy" ) instanceof FactHandle);
    }

    @Test
    public void testNoSessionInsert() throws Exception {
        Person person = new Person();
        person.setName( "Mauricio" );

        InsertObjectCommand cmd = (InsertObjectCommand) CommandFactory.newInsert( person,
                                                                                  "salaboy" );

        ExecutionResults response = (ExecutionResults) template.requestBodyAndHeader("direct:test-no-session",
                cmd,
                KieComponent.KIE_LOOKUP,
                "ksession1");
        assertTrue( "Expected valid ExecutionResults object",
                    response != null );
        assertTrue( "ExecutionResults missing expected fact",
                    response.getFactHandle( "salaboy" ) != null );
    }

    @Test
    public void testSessionBatchExecutionCommand() throws Exception {
        Person john = new Person();
        john.setName("John Smith");

        List<Command> commands = new ArrayList<Command>();
        commands.add(CommandFactory.newInsert(john, "john"));
        BatchExecutionCommand batchExecutionCommand = CommandFactory.newBatchExecution(commands);

        ExecutionResults response = (ExecutionResults) template.requestBody("direct:test-with-session",
                batchExecutionCommand);
        assertTrue( "Expected valid ExecutionResults object",
                response != null );
        assertTrue( "ExecutionResults missing expected fact",
                response.getFactHandle( "john" ) != null );
        assertTrue( "ExecutionResults missing expected fact",
                response.getFactHandle( "john" ) instanceof FactHandle);
    }

    @Test
    public void testSessionGetObject() throws Exception {
        FactHandle factHandle = new DefaultFactHandle( handle );
        GetObjectCommand cmd = (GetObjectCommand) CommandFactory.newGetObject( factHandle );
        cmd.setOutIdentifier( "rider" );

        ExecutionResults response = (ExecutionResults) template.requestBody( "direct:test-with-session",
                                                                             cmd );
        assertTrue( "Expected valid ExecutionResults object",
                    response != null );
        assertTrue( "ExecutionResults missing expected object",
                    response.getValue("rider") != null );
        assertTrue("FactHandle object not of expected type",
                response.getValue("rider") instanceof Person);
        assertEquals( "Hadrian",
                      ((Person) response.getValue( "rider" )).getName() );
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from( "direct:test-with-session" ).to( "kie://ksession1" );
                from( "direct:test-no-session" ).to( "kie://dynamic" );
            }
        };
    }

    @Override
    protected void configureDroolsContext(Context jndiContext) {
        Person me = new Person();
        me.setName( "Hadrian" );

        KieSession ksession = registerKnowledgeRuntime( "ksession1", null );
        InsertObjectCommand cmd = new InsertObjectCommand( me );
        cmd.setOutIdentifier( "camel-rider" );
        cmd.setReturnObject( false );
        BatchExecutionCommandImpl script = new BatchExecutionCommandImpl( Arrays.asList( new GenericCommand< ? >[]{cmd} ) );

        ExecutionResults results = ksession.execute( script );
        handle = ((FactHandle) results.getFactHandle( "camel-rider" )).toExternalForm();
    }
}
