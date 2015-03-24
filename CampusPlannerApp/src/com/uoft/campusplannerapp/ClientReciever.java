package com.uoft.campusplannerapp;

import java.util.Iterator;
import java.util.List;

public class ClientReciever extends Thread{
     DatabaseHandler db;
     User u;
     HTTPConsole http_console;
     List<FriendClass> friend_list;
     boolean running = true;
     
    public ClientReciever(DatabaseHandler db, User u, HTTPConsole http_console,List<FriendClass> friend_list){
    	this.db = db;
    	this.u = u;
    	this.http_console = http_console;
    	this.friend_list = friend_list;
    //	if (friends != null || friends.isEmpty()){
    	//	friends = http_console.GetFriend(u.getEmail());
	//	}
    }
    
    public void setRunning(boolean sts) {
    	System.out.println("Set running " + sts);
    	running = sts;
    }

    public void run(){
    	long timeToSleep = 10000;
        long start, end, slept;
        boolean interrupted = false;
       
        
        while(running){
        	System.out.println("Running is " + running);
        	timeToSleep = 10000;
        	while(timeToSleep > 0){
        		start=System.currentTimeMillis();
        		try{
        			if (friend_list == null || friend_list.isEmpty()){
        				break;
        			}
        			//TODO: need to perform get call here to retrieve the updated locations
        			
        			System.out.println("Receiver");
        			for (Iterator<FriendClass> iter = friend_list.iterator(); iter.hasNext(); ) {
        			    FriendClass element = iter.next();
        			    http_console.GetFriend(element.getEmail());
        			}
        			Thread.sleep(timeToSleep);
        			timeToSleep = 0;
        			break;
        		}
        		catch(InterruptedException e){

        			//work out how much more time to sleep for
                	end=System.currentTimeMillis();
                	slept=end-start;
                	timeToSleep-=slept;
                	interrupted=true;
        		}
        	}

        	if(interrupted){
        		//restore interruption before exit
        		Thread.currentThread().interrupt();
        	}
        }
	
    }
}