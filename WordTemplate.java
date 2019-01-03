package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class WordTemplate {

	public final static String version="20170622 1300";
	public static String getVersion(){return version;}		
	
	boolean isFirst = true;	
	
	String documentFoot;
	
	String [] bodytag = new String [] {"<w:body>","</w:body>"};	

	String bodytemplate;

	StringBuffer bodytemp = new StringBuffer();	
	
	FileOutputStream fous = null;
	BufferedWriter writer;
	
	File wordTemplatefilepath;
	File outfilepath;
	
	public WordTemplate(String wordTemplatefile, String outfile){		
		init(wordTemplatefile,outfile);
	}	
	
	public WordTemplate(File wordTemplatefile, File outfile){		
		init(wordTemplatefile,outfile);
	}
	
	private void init(String wordTemplatefile, String outfile){
		init( new File(wordTemplatefile),  new File(outfile)) ;
	}
		 
	private void init(File wordTemplatefile, File outfile){
		
		try {
			//init(new FileInputStream(wordTemplatefile), new FileOutputStream(outfile));
			this.wordTemplatefilepath=wordTemplatefile;
			this.outfilepath=outfile;
			this.fous=new FileOutputStream(outfilepath);
			this.writer = new BufferedWriter(new OutputStreamWriter(fous, "utf-8"));
			
			build(this.wordTemplatefilepath);
			
			
		} catch (Exception e) {			
			throw new RuntimeException(e);
		}	
	}	
	
	/*
	private void init(InputStream wordTemplatefile, OutputStream outfile) throws Exception{		
		this.writer = new BufferedWriter(new OutputStreamWriter(outfile, "utf-8"));
		build(wordTemplatefile);		
	}
	*/
	public void close()  {
		try {			
			endBody();
			writeDocumentFoot();
		}  catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			closeQuietly(this.writer);
			closeQuietly(fous);
		}
		
		
		
	}

	public void putValue(String key, String value) {
		replaceBodyContextKeyValue(bodytemp, key, value);
	}

	public void endPage() {
		try {
			addBody();
			newBodytemp();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* private */
	
	
	
	private void write(String s) throws  Exception{		
		this.writer.write(s);
		this.writer.flush();		 
	}
	
	private void addBody() throws  Exception{
		if (isFirst) {
			isFirst = false;			
			write(bodytemp.toString());		
		} else {
			write(newPage(bodytemp.toString()));				
		}
	}	
	
	private void startBody()throws  Exception{
		write(bodytag[0]);
	}
	
	private void endBody()throws  Exception{
		write(bodytag[1]);
	}	
	
	private void writeDocumentHead(String documentHead)throws  Exception{
		write(documentHead);
	}
	
	private void writeDocumentFoot()throws  Exception{
		write(documentFoot);
	}	
	
	
	
	private void build(File wordTemplatefile) throws  Exception {
		StringBuffer context = readTemplateFile(wordTemplatefile);
		int[] pos = bodyContextPostion(context);;
		this.bodytemplate = context.substring(pos[0], pos[1]);
		String documentHead = context.substring(0, pos[0]);
		this.documentFoot = context.substring(pos[1]);		
		newBodytemp();
		
		writeDocumentHead(documentHead);
		startBody();
	}

	private StringBuffer readTemplateFile(File wordTemplatefile) {
		FileInputStream fis = null ;
		BufferedReader reader = null;
		try {
			StringBuffer rs = new StringBuffer();
			fis = new FileInputStream(wordTemplatefile) ;
			reader = new BufferedReader(new InputStreamReader(
					fis, "utf-8"));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				rs.append(line);
			}
			//reader.close();
			return rs;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			closeQuietly(reader);
			closeQuietly(fis);
		}
	}

	private void newBodytemp() {
		bodytemp.delete(0, bodytemp.length()) ;
		bodytemp.append(bodytemplate);		
	}



	private String newPage(String context) {							
		StringBuilder sb = new StringBuilder(context);		
		int wp = sb.indexOf("<w:p");
		if(wp>=0){
			sb.insert(sb.indexOf(">", wp)+1, "<w:pPr><w:r><w:br w:type=\"page\"/></w:r></w:pPr>");
		}		
		return sb.toString();
	}

	private String formatKey(String key) {
		return "${" + key + "}";
	}

	private StringBuffer replaceBodyContextKeyValue(StringBuffer sb, String key,
			String value) {
		String keytag = formatKey(key);
		int pos = sb.indexOf(keytag);
		if (pos < 0) {
			return sb;
		}
		sb.replace(pos, pos + keytag.length(), value);
		return replaceBodyContextKeyValue(sb, key, value);
	}
	
	
	
	
	private int[] bodyContextPostion(StringBuffer sb) {
		String starttag = "<w:body>";
		String endtag = "</w:body>";
		int startpos = sb.indexOf(starttag) + starttag.length();
		int endpos = sb.indexOf(endtag);
		return new int[] { startpos, endpos };
	}

	private void closeQuietly(Reader input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
		}
	}
	 
	private void closeQuietly(Writer output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
		}
	}
	
	private void closeQuietly(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
		}
	}
	
	private void closeQuietly(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
		}
	}	
}
