package com.test;

import java.io.File;

public class TestWord {
	public static void main(String [] args){
		try{
			//duein.xml wttest.xml post.xml
			File wtfile = new File("./test/com/test/wttest.xml");
			File outfile = new File("./test/com/test/wtrs.doc");
			WordTemplate wt = new WordTemplate(wtfile,outfile);
			wt.putValue("test", "這是test!!!-1");
			wt.putValue("attr1", "這是attr1!!!-1");
			wt.putValue("attr2", "這是attr2!!!-1");
			wt.endPage();			
			wt.putValue("test", "這是test!!!-2");
			wt.putValue("attr1", "這是attr1!!!-2");
			wt.putValue("attr2", "這是attr2!!!-2");
			wt.endPage();
			wt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
