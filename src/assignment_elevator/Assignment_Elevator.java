/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment_elevator;

import java.util.Scanner;


public class Assignment_Elevator{

    public static void main(String[] args) {
        
        Scanner input = new Scanner(System.in);
        Variable elevator1 = new Variable();
        
        System.out.print("Number of request :");
        int request = input.nextInt();
        System.out.print("Enter capacity of elevator :");
        int capacityElevator = input.nextInt();
        while(capacityElevator<=0) {
            System.out.print("Invalid value of capacity. Please reenter: ");
            capacityElevator = input.nextInt();
        }
        System.out.print("Enter maximum waiting time of elevator :");
        int waitingTime = input.nextInt();
        while(waitingTime<=0) {
            System.out.print("Invalid value of capacity. Please reenter: ");
            waitingTime = input.nextInt();
        }
        
        int time = 0;
        int[] id = new int[request];
        int[] requestTime = new int[request];
        int[] srcFloor = new int[request];
        int[] destFloor= new int[request]; 
        int[] duration= new int[request]; 
                  
        for(int i=0; i<request; i++){
            
            System.out.print("\nRequest ID : ");
            id[i]=input.nextInt();
            
            System.out.print("Request time : ");
            requestTime[i]=input.nextInt();
            
            System.out.print("Source floor : ");
            srcFloor[i]=input.nextInt();
            while(srcFloor[i]>10 || srcFloor[i]<0)
            {System.out.print("Invalid floor. Please reenter: ");
            srcFloor[i] = input.nextInt();}
            
            System.out.print("Destination : ");
            destFloor[i]=input.nextInt();
            while(destFloor[i]>10 || destFloor[i]<0)
            {System.out.print("Invalid floor. Please reenter: ");
            destFloor[i] = input.nextInt();}
            
            duration[i]=28+(Math.abs(destFloor[i]-srcFloor[i]));
            
            if(i==0){ time+=(srcFloor[i]+requestTime[i]+28+(Math.abs(destFloor[i]-srcFloor[i]))); }
            
            else{
                if(requestTime[i]>time){ time+=((requestTime[i]-time)+(28+(Math.abs(destFloor[i]-srcFloor[i]))+(Math.abs(destFloor[i-1]-srcFloor[i])))); }
                else{ time+=((28+(Math.abs(destFloor[i]-srcFloor[i]))+(Math.abs(destFloor[i-1]-srcFloor[i])))); }
            }
            
            int a = id[i];
            int b = requestTime[i];
            int c = srcFloor[i];
            int d = destFloor[i];
            int e = duration[i];
            
            elevator1.add(a, b, c, d, e);    
        }
        
        elevator1.add(request, time, capacityElevator, waitingTime);
        elevator1.Calculation();
        elevator1.Message();
        
    }
    
}
