package com.melvold.sms.dbinterface;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.melvold.sms.communication.Communication;

public class DBInterface {

	public Byte salt;
	public Communication communication;

	public DBInterface(String host, String bid, String password){
		this.salt = null;
		this.communication = null;
		this.communication = new Communication(host, bid, password, 443, "https");
	}

	public void testConnection(){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("bid", "lam002"));
		nvp.add(new BasicNameValuePair("bid2", "iam001"));
		ArrayList<ArrayList<String>> ans = this.communication.post("test.php", nvp);
		String test = "";
		for(int i = 0; i < ans.size(); i++){
			for(int j = 0; j < ans.get(i).size(); j++){
				test+= ans.get(i).get(j) + ", ";
			}
			test+="\n-------------------------\n";
		}
		System.out.println(test);

	}

	public ArrayList<ArrayList<String>> listAllGroups(){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("select1", "g_id"));
		nvp.add(new BasicNameValuePair("select2", "g_navn"));
		nvp.add(new BasicNameValuePair("table1", "grupper"));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> listAllMembersWithTlf(){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("select1", "Fornavn"));
		nvp.add(new BasicNameValuePair("select2", "Etternavn"));
		nvp.add(new BasicNameValuePair("select3", "tlf"));
		nvp.add(new BasicNameValuePair("select4", "type"));
		nvp.add(new BasicNameValuePair("table1", "users"));
		nvp.add(new BasicNameValuePair("table2", "tlf_nr"));
		nvp.add(new BasicNameValuePair("users/tlf", "u_id"));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> listAllMembers(){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("select1", "Fornavn"));
		nvp.add(new BasicNameValuePair("select2", "Etternavn"));
		nvp.add(new BasicNameValuePair("table1", "users"));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> listMembersInGroup(String groupID){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("select1", "Fornavn"));
		nvp.add(new BasicNameValuePair("select2", "Etternavn"));
		nvp.add(new BasicNameValuePair("table1", "users"));
		nvp.add(new BasicNameValuePair("table2", "g_kobling"));
		nvp.add(new BasicNameValuePair("table3", "grupper"));
		nvp.add(new BasicNameValuePair("users/group", groupID));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> listReceivedMessages(String maxPerPage, String pageNum){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		//Chosen fields
		nvp.add(new BasicNameValuePair("select1", "sender"));
		nvp.add(new BasicNameValuePair("select2", "Fornavn"));
		nvp.add(new BasicNameValuePair("select3", "Etternavn"));
		nvp.add(new BasicNameValuePair("select4", "msg"));
		//Order by time
		nvp.add(new BasicNameValuePair("select5", "receivedtime"));
		//Join on and group by message id
		nvp.add(new BasicNameValuePair("select6", "id"));
		nvp.add(new BasicNameValuePair("table1", "ozekimessagein"));
		nvp.add(new BasicNameValuePair("table2", "tlf_nr"));
		nvp.add(new BasicNameValuePair("table3", "users"));
		//Limit result by maxPerPage
		nvp.add(new BasicNameValuePair("msgs_received", maxPerPage));
		//Choose pageNum
		nvp.add(new BasicNameValuePair("page", pageNum));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> listReceivedMessagesByCode(String maxPerPage, String pageNum, String code){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		//Chosen fields
		nvp.add(new BasicNameValuePair("select1", "sender"));
		nvp.add(new BasicNameValuePair("select2", "Fornavn"));
		nvp.add(new BasicNameValuePair("select3", "Etternavn"));
		//Message to search for code in
		nvp.add(new BasicNameValuePair("select4", "msg"));
		//Order by time
		nvp.add(new BasicNameValuePair("select5", "receivedtime"));
		//Join on and group by message id
		nvp.add(new BasicNameValuePair("select6", "id"));
		nvp.add(new BasicNameValuePair("table1", "ozekimessagein"));
		nvp.add(new BasicNameValuePair("table2", "tlf_nr"));
		nvp.add(new BasicNameValuePair("table3", "users"));
		//Code to search for in message
		nvp.add(new BasicNameValuePair("code", code));
		//Limit result by maxPerPage
		nvp.add(new BasicNameValuePair("msgs_received", maxPerPage));
		//Choose pageNum
		nvp.add(new BasicNameValuePair("page", pageNum));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> listCompleteMessagesByType(String type){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("select1", "m_id"));
		nvp.add(new BasicNameValuePair("select2", "name"));
		nvp.add(new BasicNameValuePair("table1", "messages"));
		nvp.add(new BasicNameValuePair("messagetype", type));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> listCompleteMessageByID(String id){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("select1", "name"));
		nvp.add(new BasicNameValuePair("select2", "message"));
		nvp.add(new BasicNameValuePair("table1", "messages"));
		nvp.add(new BasicNameValuePair("m_id", id));
		return this.communication.post("list.php", nvp);
	}

	public ArrayList<ArrayList<String>> sendMessage(String from, String to, String message){
		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("field1", "sender"));
		nvp.add(new BasicNameValuePair("field2", "receiver"));
		nvp.add(new BasicNameValuePair("field3", "msg"));
		nvp.add(new BasicNameValuePair("field4", "status"));
		nvp.add(new BasicNameValuePair("field5", "msgtype"));
		nvp.add(new BasicNameValuePair("value1", from));
		nvp.add(new BasicNameValuePair("value2", to));
		nvp.add(new BasicNameValuePair("value3", message));
		nvp.add(new BasicNameValuePair("value4", "send"));
		nvp.add(new BasicNameValuePair("value5", "SMS:TEXT"));
		nvp.add(new BasicNameValuePair("table1", "ozekimessageout"));
		return this.communication.post("insert.php", nvp);
	}

	public boolean logOut(){

		return true;
	}

	public static void main(String[] args){
		DBInterface dbi = new DBInterface("localhost", "lam002", "mikkel96");
		//dbi.testConnection();
		//dbi.listAllGroups();
		//dbi.listAllMembersWithTlf();
		//dbi.listAllMembers();
		//dbi.listMembersInGroup("1");
		//dbi.listReceivedMessages("5", "1");
		//dbi.listReceivedMessagesByCode("5", "1", "bst");
		//dbi.listCompleteMessagesByType("Forvaltning");
		//dbi.listCompleteMessageByID("1");
		//dbi.sendMessage("12345678", "87654321", "Hello World!");
	}

}
