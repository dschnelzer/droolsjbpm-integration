package org.drools.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;

import org.acord.standards.pc_surety.acord1.xml.AssignedIdentifier;
import org.acord.standards.pc_surety.acord1.xml.ClientAppType;
import org.acord.standards.pc_surety.acord1.xml.CustIdType;
import org.acord.standards.pc_surety.acord1.xml.DateTime;
import org.acord.standards.pc_surety.acord1.xml.HomePolicyQuoteInqRsType;
import org.acord.standards.pc_surety.acord1.xml.InsuranceSvcRsType;
import org.acord.standards.pc_surety.acord1.xml.MessageStatus;
import org.acord.standards.pc_surety.acord1.xml.MsgStatusType;
import org.acord.standards.pc_surety.acord1.xml.OpenEnum;
import org.acord.standards.pc_surety.acord1.xml.SignonRsType;
import org.acord.standards.pc_surety.acord1.xml.StatusType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Message;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.mcarta.app.ACORDRs;

public class SOAPProcessor   {
	
	private JAXBContext jaxbContext = null;
	public CxfPayload processSOAP(Exchange exchange) {
		
		Object inBody = exchange.getIn().getBody();
		System.out.println("In processSOAP:" + inBody.getClass().getName());
		CxfPayload inPayload = (CxfPayload)inBody;
		
		
		
		List bodyList = inPayload.getBody();
		Element rootElement = (Element)bodyList.get(0);
		System.out.println("after root element");		
		try {
			System.out.println("Before JAXB Contest");
			if(jaxbContext == null) {
				jaxbContext = JAXBContext.newInstance("com.mcarta.app");				
			}
			System.out.println("After JAXB Contest");
			//Create unmarshaller
			System.out.println("before unmarshal");
			Unmarshaller um = jaxbContext.createUnmarshaller();
			System.out.println("after unmarshall");
			//Unmarshal XML contents of the file myDoc.xml into your Java object instance.
		//	ACCORDRq myJAXBObject = (ACCORDRq) 
//			um.unmar
//			um.unmarshal(new java.io.FileInputStream( "myDoc.xml" ));
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		

/*
		<SignonRs>
			<Status>
				<StatusCd>0</StatusCd>
				<StatusDesc>Good</StatusDesc>
			</Status>
			<CustId>
				<SPName>ADMIN</SPName>
				<CustPermId>456</CustPermId>
				<CustLoginId>123</CustLoginId>
			</CustId>
			<ClientDt>2010-10-10</ClientDt>
			<CustLangPref>ENG</CustLangPref>
			<ClientApp>
				<Org>AEM</Org>
				<Name>Vizuri</Name>
				<Version>2.1</Version>
			</ClientApp>	
			<ServerDt>2012-12-12</ServerDt>
			<ExpDt>2013-12-12</ExpDt>
			<Language>ENG</Language>
		</SignonRs>
*/
		
		
		
		
		
		
		ACORDRs response = new ACORDRs();
		SignonRsType signonRs = new SignonRsType();
		StatusType statusType = new StatusType();
		statusType.setStatusCd("0");
		statusType.setStatusDesc("Good");
		signonRs.setStatus(statusType);
		CustIdType custIdType = new CustIdType();
		//AssignedIdentifier ai = new AssignedIdentifier();
		custIdType.setCustPermId("456");
		custIdType.setCustLoginId("123");
		signonRs.setCustId(custIdType);
		signonRs.setClientDt("2010-10-10");
		signonRs.setCustLangPref("ENG");
		ClientAppType clientAppType = new ClientAppType();
		clientAppType.setName("Vizuri");
		clientAppType.setOrg("AEM");
		clientAppType.setVersion("2.1");
		signonRs.setClientApp(clientAppType);
		signonRs.setLanguage("ENG");
		signonRs.setExpDt("2012-12-12");
		signonRs.setServerDt("2012-12-12");
		

/*
 	<InsuranceSvcRs>
		<Status>
			<StatusCd>0</StatusCd>
			<StatusDesc>Good</StatusDesc>
		</Status>
		<RqUID>12345678-1234-1234-1234-123456789012</RqUID>
		<SPName>ADMIN</SPName>
		<HomePolicyQuoteInqRs>
			<RqUID>12345678-1234-1234-1234-123456789012</RqUID>
			<ItemIdInfo>
				<InsurerId>1234</InsurerId>
			</ItemIdInfo>
			<TransactionResponseDt>2013-06-06</TransactionResponseDt>
			<TransactionEffectiveDt>2013-08-02</TransactionEffectiveDt>
			<CurCd>USD</CurCd>
			<MsgStatus>
				<MsgStatusCd>Success</MsgStatusCd>
			</MsgStatus>
		</HomePolicyQuoteInqRs>		
	</InsuranceSvcRs>

 */
		response.setSignonRs(signonRs);
        
		InsuranceSvcRsType insuranceSvcRs = new InsuranceSvcRsType();
		
		insuranceSvcRs.setStatus(statusType);
		insuranceSvcRs.setRqUID("12345678-1234-1234-1234-123456789012");
		HomePolicyQuoteInqRsType homePolicyQuoteInqRsType = new HomePolicyQuoteInqRsType();
		homePolicyQuoteInqRsType.setRqUID("12345678-1234-1234-1234-123456789012");
		homePolicyQuoteInqRsType.setTransactionEffectiveDt(new DateTime());
		homePolicyQuoteInqRsType.setTransactionResponseDt(new DateTime());
	//	insuranceSvcRs.
		OpenEnum openEnum = new OpenEnum();
		//openEnum.
		MsgStatusType msgStatusType = new MsgStatusType();
		MessageStatus messageStatus = new MessageStatus();
		messageStatus.setCodeListRef("111");
		messageStatus.setValue("Success");
		
		
		msgStatusType.setMsgStatusCd(messageStatus);
		homePolicyQuoteInqRsType.setMsgStatus(msgStatusType);
		
		response.setInsuranceSvcRs(insuranceSvcRs);
		com.mcarta.app.ObjectFactory of = new com.mcarta.app.ObjectFactory();
		JAXBElement<ACORDRs> rElement =  of.createACORDRs(response);
		Marshaller marshaller;
		Element responseElement = null;
		try {
			marshaller = jaxbContext.createMarshaller();
			DOMResult res = new DOMResult();
			marshaller.marshal(rElement, res);
			responseElement = ((Document)res.getNode()).getDocumentElement();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Create Response 2");
		
		
		List<Element> responseList = new ArrayList<Element>();
		responseList.add(responseElement);
		
		CxfPayload responsePayload = new CxfPayload(inPayload.getHeaders(), responseList);
		return responsePayload;
	}

}
