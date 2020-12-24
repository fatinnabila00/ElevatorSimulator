package assignment_elevator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class Variable {
    
    public static int request;
    public static int time;
    public static java.util.ArrayList id = new java.util.ArrayList<>();
    public static java.util.ArrayList requestTime = new java.util.ArrayList<>();
    public static java.util.ArrayList srcFloor = new java.util.ArrayList<>();
    public static java.util.ArrayList destFloor = new java.util.ArrayList<>(); 
    public static java.util.ArrayList duration = new java.util.ArrayList<>();
    public static java.util.ArrayList message = new java.util.ArrayList<>();
    public static java.util.ArrayList direction = new java.util.ArrayList<>();
    public static java.util.ArrayList statusHeading = new java.util.ArrayList<>();
    public static java.util.ArrayList statusArrived = new java.util.ArrayList<>();
    public static java.util.ArrayList quantity = new java.util.ArrayList<>();
    public static java.util.ArrayList quantityIndex = new java.util.ArrayList<>();
    public static java.util.ArrayList utilisedTime = new java.util.ArrayList<>();
    public static int heading, opening1, opening2, opened1, opened2, closing1, closing2, closed1, closed2, serviceRequest;
    public static int currentfloor=0;
    public static int attended=0;
    public static int waitingTime=0;
    int amount=1;
    int floorstravel=0;
    int currentCapacity=0;
    int capacityElevator=0;
    
    
    public Variable (){
        
    }
    
    public void add(int request, int time, int capacityElevator, int waitingTime){
        this.request = request;
        this.time = time;
        this.capacityElevator=capacityElevator;
        this.waitingTime = waitingTime;
        for(int i=0; i<=time; i++){
            message.add("NULL");
        }
        
        for(int i=0; i<request; i++){
            statusHeading.add(0);
            statusArrived.add(0);
            quantity.add(1);
            utilisedTime.add(0);
        }
    }
    
    public void add(int a, int b, int c, int d, int e){
        id.add(a);
        requestTime.add(b);
        srcFloor.add(c);
        destFloor.add(d);
        duration.add(e);
        
    }
       
    public void Calculation() {
        sorting();
        findDirection();
        
        for(int i=0; i<request; i++){
            invokeMessage("requestTime", (int)requestTime.get(i), i);
            boolean status = true;
            boolean exceed = false;
            if((statusHeading.get(i).equals(0)) && (currentCapacity<(capacityElevator))){
                status = false;
                if(Math.abs(closed2-(int)requestTime.get(i))>waitingTime){exceed=true;}
                if(exceed==false){
                attended++;
                opening(1, i, 0);
                currentCapacity++;
                opened(1, i);
                closing(1, i);
                closed(1, i);
                checkFloor(i);
                }
            }
            
            if(statusArrived.get(i).equals(0) && exceed==false){
                if(status==true){ 
                    Variable.closed1=Variable.closed2+Math.abs((int)destFloor.get(i)-(int)destFloor.get(i-1)); 
                    floorstravel+=Math.abs((int)destFloor.get(i)-(int)destFloor.get(i-1));
                }
                else if(i==0){ floorstravel+=(int)destFloor.get(i); }
                else { floorstravel+=(Math.abs((int)srcFloor.get(i)-(int)destFloor.get(i-1))+Math.abs((int)srcFloor.get(i)-(int)destFloor.get(i))); }
                opening(2, i, 0);
                opened(2, i);
                closing(2, i);
                closed(2, i);
                for(int a=0; a<quantityIndex.size(); a++){ utilisedTime.set((int)quantityIndex.get(a), closed2-(int)requestTime.get((int)quantityIndex.get(a)));}
                quantityIndex.clear();
            }
        }
    }
    
    public void Message(){
        try{
            File file = new File("logfile.txt");
            PrintWriter write = new PrintWriter(file);
            BufferedWriter bw = new BufferedWriter(write);
            write.println("");
            for(int i=0; i<message.size(); i++){
                if(!message.get(i).equals("NULL")){
                    write.println(message.get(i));
                }
            }
        write.close();
        bw.close();
        }catch(IOException e){
            System.out.println(e);
        }
        
        try{
            File file = new File("reportfile.txt");
            PrintWriter write = new PrintWriter(file);
            BufferedWriter bw = new BufferedWriter(write);
            write.println("");
            write.println("");
            write.println("###Elevator statistic###");
            write.println("Service request processed : "+request);
            write.println("Passenger served : "+request);
            write.println("Total floors traveled : "+ floorstravel);
            write.println("Total time taken : "+closed2);
            System.out.println("");
            for(int i=0; i<request; i++){
                write.println("## Request ID - "+i);
                write.println("Total utilized time : "+utilisedTime.get(i));
            }
        write.close();
        bw.close();
        }catch(IOException e){
            System.out.println(e);
        }
        
        for(int i=0; i<message.size(); i++){
            if(!message.get(i).equals("NULL")){
                System.out.println(message.get(i));
            }
        }
        
        System.out.println("");
        System.out.println("\u001b[30;46m###Elevator statistic###\u001b[0m");
        System.out.println("\u001b[35;1mService request processed : \u001b[0m"+request);
        System.out.println("\u001b[35;1mPassenger served : \u001b[0m"+attended);
        System.out.println("\u001b[35;1mTotal floors traveled : \u001b[0m"+ floorstravel);
        System.out.println("\u001b[35;1mTotal time taken : \u001b[0m"+closed2);
        System.out.println("");
        for(int i=0; i<request; i++){
            System.out.println("## Request ID - "+id.get(i));
            System.out.println("Total utilized time : "+utilisedTime.get(i));
        }
    }

    private void checkFloor(int j) {
        
        java.util.ArrayList y = new java.util.ArrayList<>();
               
        for(int i=(int)srcFloor.get(j); i<(int)destFloor.get(j); i++){
            for(int x=j+1; x<request; x++){
                boolean exceed = false;
                if((direction.get(x).equals(direction.get(j))) && (currentCapacity<capacityElevator)){
                    if(Math.abs(closed1-(int)requestTime.get(x))>waitingTime){ exceed=true; }               
                    if(i==(int)srcFloor.get(x) && closed1>(int)requestTime.get(x) && (exceed==false)){
                        opening(3, x, Variable.closed1);
                        attended++;
                        opened(1, x);
                        closing(1, x);
                        if(y.size()>0){
                            for(int a=0; a<y.size(); a++){
                                if((int)destFloor.get((int) y.get(a))<(int)destFloor.get(j) && (i==(int)destFloor.get((int) y.get(a)))){
                                    String copy = (String) message.get(Variable.closing1);
                                    message.set(Variable.closing1, Variable.closing1+": 1 passenger(s) left the elevator.\n"+copy);
                                    statusArrived.set((int) y.get(a), -1);
                                    currentCapacity--;
                                }
                            }
                        }
                        closed(1, x);
                        currentCapacity++;
                        Variable.currentfloor=i;
                        statusHeading.set(x, -1);
                        y.add(x);
                    }
                }
            }
            
            for(int a=0; a<y.size();a++){
                amount=1;
                boolean status=false;
                if(y.size()>1 && a<y.size()-1){
                    for(int b=a; b<y.size()-1; b++){
                        if(destFloor.get((int)y.get(a)).equals(destFloor.get((int)y.get(b+1))) && i==(int)destFloor.get((int)y.get(b))){
                            amount++;
                            statusArrived.set((int) y.get(b+1), -1);
                            quantity.set((int) y.get(a), amount);
                        }
                    }
                }
                
                if(direction.get((int)y.get(a)).equals(direction.get(j))){
                    if((int)destFloor.get((int)y.get(a))==(int)destFloor.get(j)){
                        if(statusArrived.get((int) y.get(a)).equals(0)){
                            if((int)quantity.get(j)<request){
                            amount++;
                            quantity.set(j,amount);
                            statusArrived.set((int) y.get(a), -1);
                            quantityIndex.add((int)y.get(a));
                            }
                        }
                    }
                    
                    if((int)destFloor.get((int) y.get(a))<(int)destFloor.get(j) && (i==(int)destFloor.get((int) y.get(a)))){
                        if(statusArrived.get((int)y.get(a)).equals(0)){
                            opening(4,(int)y.get(a),Variable.closed1);
                            opened(2,(int)y.get(a));
                            closing(2,(int)y.get(a));
                            closed(3,(int)y.get(a));
                            statusArrived.set((int) y.get(a), -1);
                            for(int b=a; b<y.size()-1; b++){
                                if(destFloor.get((int)y.get(a)).equals(destFloor.get((int)y.get(b+1)))){
                                    utilisedTime.set(b+1, closed2-(int)requestTime.get(b+1));
                                }
                            }
                            Variable.closed1=Variable.closed2;
                        }
                        Variable.currentfloor=i;
                    }
                }
            }
            Variable.closed1++;
        }
    }

    private void findDirection() {
        for(int i=0; i<request; i++){
            if(((int)destFloor.get(i)-(int)srcFloor.get(i))>0){
                direction.add("Upwards");
            }
            else{
                direction.add("Downwards");
            }
        }
    }

    private void opening(int x, int index, int value) {
        int type=x;

        if(type==1){ 
            if(index==0 & srcFloor.get(index).equals(0)){ opening1=(int) requestTime.get(index);}
            else if(index==0 & !srcFloor.get(index).equals(0)){
                Variable.opening1=(int)requestTime.get(index)+(int)srcFloor.get(index);
                message.set(opening1, opening1+": Reached at floor "+srcFloor.get(index));
            }
            else if(index>0 && (int)requestTime.get(index)>(int)duration.get(index-1)){
                Variable.opening1=(int)requestTime.get(index)+Math.abs((int)destFloor.get(index-1)-(int)srcFloor.get(index));
                message.set(opening1, opening1+": Reached at floor "+srcFloor.get(index));
            }
            else{ Variable.opening1=Variable.closed2+Math.abs((int)destFloor.get(index-1)-(int)srcFloor.get(index)); }
            invokeMessage("opening1",Variable.opening1, index);
        }
        
        if(type==2){ 
            int h=0;
            Variable.opening2=Variable.closed1;
            invokeMessage("opening2", Variable.opening2, index);
        }
        if(type==3){
            Variable.opening1=value;
            invokeMessage("opening3", Variable.opening1, index);
        }
        if(type==4){
            Variable.opening2=value;
            invokeMessage("opening2", Variable.opening2, index);
        }
    }

    private void opened(int x, int index) {
        int type=x;
        if(type==1){ 
            Variable.opened1 = Variable.opening1+5;
            invokeMessage("opened1", Variable.opened1, index);
        }
        if(type==2){ 
            Variable.opened2 = Variable.opening2+5;
            invokeMessage("opened2", Variable.opened2, index);
        }
        
    }

    private void closing(int x, int index) {
        int type=x;
        if(type==1){
            Variable.closing1 = Variable.opened1+4;
            invokeMessage("closing1", Variable.closing1, index);
        }
        if(type==2){
            Variable.closing2 = Variable.opened2+4;
            invokeMessage("closing2", Variable.closing2, index);
        }
    }

    private void closed(int x, int index) {
        int type=x;
        if(type==1){ 
            Variable.closed1 = Variable.closing1+5;
            invokeMessage("closed1", Variable.closed1, index);
        }
        if(type==2){ 
            Variable.closed2 = Variable.closing2+5;
            utilisedTime.set(index, closed2-(int)requestTime.get(index));
            invokeMessage("closed2", Variable.closed2, index);
        }
        if(type==3){
           Variable.closed2 = Variable.closing2+5;
           utilisedTime.set(index, closed2-(int)requestTime.get(index));
           invokeMessage("closed3", Variable.closed2, index); 
        }
    }

    private void invokeMessage(String type, int value, int index) {
        String text=" ";
        switch(type){
            case "requestTime": if((index==0 && !srcFloor.get(index).equals(0)) || (index>0 && (int)requestTime.get(index)>(int)duration.get(index-1))){
                                    text = requestTime.get(index)+": Service Request (Request ID:"+id.get(index)+") received from floor "+srcFloor.get(index)+" to floor "+destFloor.get(index)
                                    +".\n"+requestTime.get(index)+": Heading to floor "+srcFloor.get(index);
                                }
                                else{ text = requestTime.get(index)+": Service Request (Request ID:"+id.get(index)+") received from floor "+srcFloor.get(index)+" to floor "+destFloor.get(index);}
                                break;
            case "opening1":text = value+": Door opening";
                            break;
            case "opening2":text = value+": Reached floor "+destFloor.get(index)+"\n"+value+": Door opening";
                            break;
            case "opening3":text = value+": Stopped at floor "+srcFloor.get(index)+"\n"+value+": Door opening";
                            break;
            case "opened1" :text = value+": Door opened";
                            break;
            case "opened2" :text = value+": Door opened";
                            break;
            case "closing1":text = value+": 1 passenger(s) entered the elevator\n"+value+": Door closing";
                            break;
            case "closing2":text = value+": "+quantity.get(index)+" passenger(s) left the elevator\n"+value+": Door closing";
                            for(int k=0; k<(int)quantity.get(index); k++){ currentCapacity--; }
                            break;
            case "closed1" :if(request>1 && index<request){ text = value+": Door closed\n"+value+": Heading to floor "+destFloor.get(index); }
                            else{ text = value+": Door closed."; }
                            break;
            case "closed2" :text=value+": Door closed";
                            if(request>1 && index<request-1){
                                if(statusHeading.get(index+1).equals(0) && ((int)requestTime.get(index+1)<(int)duration.get(index))){
                                text = value+": Door closed\n"+value+": Heading to floor "+srcFloor.get(index+1);
                                }
                            }
                            break;
            case "closed3" :text=value+": Door closed";
                            break;
        }
        if(message.get(value).equals("NULL")){ message.set(value, text); }
        else{
            String copy = (String) message.get(value);
            message.set(value, copy+"\n"+text);
        }
    }
    
    public static void sorting() {
        for(int i=0; i<request; i++){
            int index=i;
            int min = (int) requestTime.get(i);
            for(int j=i; j<request; j++){
                if((int)requestTime.get(j)<min){
                    min=(int) requestTime.get(j);
                    index=j;
                }
            }
            int temp1 = (int) id.get(i);
            int temp2 = (int) requestTime.get(i);
            int temp3 = (int) srcFloor.get(i);
            int temp4 = (int) destFloor.get(i);
            
            id.set(i, id.get(index));
            requestTime.set(i, requestTime.get(index));
            srcFloor.set(i, srcFloor.get(index));
            destFloor.set(i, destFloor.get(index));
            
            id.set(index, temp1);
            requestTime.set(index, temp2);
            srcFloor.set(index, temp3);
            destFloor.set(index, temp4);
        }
    }

}