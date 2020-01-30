package com.abg.passkey;

import java.io.*;
import java.util.*;
import de.brendamour.jpasskit.*;
import de.brendamour.jpasskit.signing.*;
import de.brendamour.jpasskit.passes.*;
import de.brendamour.jpasskit.enums.*;
import java.util.zip.*;
import org.apache.commons.io.IOUtils;
import java.nio.charset.Charset;
import java.awt.Color;

public class GeneratePass 
{
	public static void main(String[] args) {
	  PKPass pass = PKPass.builder()
		.pass(
				PKGenericPassBuilder.builder()
						.passType(PKPassType.PKBoardingPass)
						.transitType(PKTransitType.PKTransitTypeGeneric)
						.headerFieldBuilder(
								PKField.builder()
										.key("rentalNumber")
										.label("Rental #")
										.value("1234-567")
						).primaryFieldBuilder(
								PKField.builder()
										.key("starts")
										.label("STARTS")
										.value("12/30")
						).primaryFieldBuilder(
								PKField.builder()
										.key("ends")
										.label("ENDS")
										.value("12/31")

						).auxiliaryFieldBuilder(
								PKField.builder()
										.key("startTime")
										.label("Start Time")
										.value("8:30PM")
						).auxiliaryFieldBuilder(
								PKField.builder()
										.key("returnTime")
										.label("Return Time")
										.value("8:30PM")
						).secondaryFieldBuilder(
								PKField.builder()
										.key("license")
										.label("License")
										.value("WUJ 2L")
						).secondaryFieldBuilder(
								PKField.builder()
										.key("make")
										.label("Make")
										.value("Equinox")
						).secondaryFieldBuilder(
								PKField.builder()
										.key("color")
										.label("Color")
										.value("Red")
						).backFieldBuilder(
								PKField.builder()
										.key("returnAddress")
										.label("Return Address")
										.value("Avis Car Rental, San Francisco International Airport, 780 N McDonnell Rd, San Francisco, CA 94128")
						).backFieldBuilder(
								PKField.builder()
										.key("terms")
										.label("TERMS AND CONDITIONS")
										.value("Avis offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"asis\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. Generico reserves the right to change or discontinue at any time any aspect or feature of the pass.")
						)
		)
		.barcodeBuilder(
				PKBarcode.builder()
						.format(PKBarcodeFormat.PKBarcodeFormatPDF417)
						.message("Avis Pass")
						.messageEncoding(Charset.forName("utf-8"))
		)
		.formatVersion(1)
		.passTypeIdentifier("pass.ci7.com.avis.passkey")
		.serialNumber("gT6zrHkaW")
		.teamIdentifier("YL7ZB6XY59")
		.organizationName("Avis Budget Group")
		.logoText("PassKey")
		.description("My Avis Pass")
		.backgroundColor(Color.DARK_GRAY)
		.foregroundColor(Color.WHITE)
		.labelColor(Color.WHITE)
		.build();
		
		try{
			createPass(pass);
		}
		catch(Exception e){}
	}
  
	public static void createPass(PKPass pass) throws Exception {
		String keyStorePath = "/Users/rwright/desktop/passkey/Certificates.p12";
		String keyStorePassword = "passkey";
		String appleWWDRCA = "/Users/rwright/desktop/passkey/pass.cer";
		String template_path = "/Users/rwright/desktop/passkey/Avis.pass";
		String outputFile = "/Users/rwright/desktop/passkey/output/Avis.pkpass";

		PKSigningInformation pkSigningInformation = new  PKSigningInformationUtil().loadSigningInformationFromPKCS12AndIntermediateCertificate(keyStorePath,  keyStorePassword, appleWWDRCA);
		PKPassTemplateFolder passTemplate = new PKPassTemplateFolder(template_path);
		PKFileBasedSigningUtil pkSigningUtil = new PKFileBasedSigningUtil();
		byte[] signedAndZippedPkPassArchive = pkSigningUtil.createSignedAndZippedPkPassArchive(pass, passTemplate, pkSigningInformation);
	
		ByteArrayInputStream inputStream = new ByteArrayInputStream(signedAndZippedPkPassArchive);
		IOUtils.copy(inputStream, new FileOutputStream(outputFile));
		/*
		try (PrintWriter out = new PrintWriter("/Users/rwright/desktop/passkey/output/success.txt")) {
			out.println("Pass Created Successfully!");
		}
		*/
	
	}
}
