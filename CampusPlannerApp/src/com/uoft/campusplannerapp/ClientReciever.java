package com.uoft.campusplannerapp;

public class ClientReciever extends Thread{

    public ClientReciever(){
	;
    }

    public void run(){
    	long timeToSleep = 10000;
        long start, end, slept;
        boolean interrupted = false;
        
        while(true){
        	timeToSleep = 10000;
        	while(timeToSleep > 0){
        		start=System.currentTimeMillis();
        		try{
            	
        			//TODO: need to perform get call here to retrieve the updated locations
        			System.out.println("Receiver");

        			Thread.sleep(timeToSleep);
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