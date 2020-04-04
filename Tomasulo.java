import java.util.ArrayList;
import java.util.Scanner;

public class Tomasulo {

    static int itr=1;
    static String[] Reg = new String[13];
    static String[] Load = new String[3];
    static String[] Store = new String[2];
    static int NoOfStr;
    static int clock=0;
    static String[] Inst= new String[15];
    static ArrayList<String> Waiting=new ArrayList();
    static ArrayList<Integer> WaitingInt=new ArrayList();

    public static void main(String[] args) {

        /* Declaring the reservation station. */

        String[][] ReservationStation = new String [6][8];

        /* Fill the first row of the reservation table with headings. */

        ReservationStation[0][0]="Time";ReservationStation[0][1]="Name";ReservationStation[0][2]="Busy";ReservationStation[0][3]="Op  ";
        ReservationStation[0][4]="Vj  ";ReservationStation[0][5]="Vk  ";ReservationStation[0][6]="Qj  ";ReservationStation[0][7]="Qk  ";

        /* Initializing the register, load and store arrays. */

        for(int i=0 ; i<13 ; i++)Reg[i]="0";
        for(int i=0 ; i<3 ; i++)Load[i]="0";
        for(int i=0 ; i<2 ; i++)Store[i]="0";

        System.out.println("");

        /* Assigning values to the reservation table. */

        for(int i=1 ; i <=5 ; i++){

            /* Assign values to 2nd and 3rd column.*/

            if (i<=3){
                ReservationStation[i][1]="Add ";
                ReservationStation[i][2]="No  ";
            }
            else{
                ReservationStation[i][1]="Mult";
                ReservationStation[i][2]="No  ";
            }
        }
        for(int i=1 ; i<6 ; i++)

            /* Fill the remaining columns with zeros initially. */
            for(int j=0;j<7;j++){
                if (j>0 && j<3)continue;
                ReservationStation[i][j]="0   ";
            }


        /* Printing the reservation system. */
        for(int i=0 ; i<=5; i++){
            for(int j=0;j<7;j++){
                System.out.print(ReservationStation[i][j]+"\t\t");
            }
            System.out.println("");
        }

        int[] InstType = new int [15];
        int time;

        String in;
        Scanner sc = new Scanner (System.in);
        System.out.println("\nEnter number of instructions: ");
        NoOfStr = Integer.parseInt(sc.nextLine());
        int[] IssuanceArray = new int[NoOfStr];

        /* Declaring the Instruction Status table. */

        String[][] instructionStatusTable = new String[NoOfStr+1][4];

        /* Initialize the first row with headings. */

        instructionStatusTable[0][0]="Instructions";instructionStatusTable[0][1]="IssueInstruction";
        instructionStatusTable[0][2]="Complete Execution";instructionStatusTable[0][3]="WriteBack";

        /* Fill the table initially with -5 except the first row and first column. */
        for(int i=1 ; i<NoOfStr+1 ; i++){
            for(int j=1 ; j<4 ; j++)instructionStatusTable[i][j]="-5";
        }


        /* input instructions. */
        for (int i = 0; i < NoOfStr; i++){
            System.out.println("Enter the instruction #"+(i+1)+": ");

            /* Assign value=-50 for the current instruction in issuance array. */

            IssuanceArray[i]=-50;
            in = sc.nextLine();
            Inst[i]=in;
        }
        while(clock<NoOfStr){

            for(int DupClock=clock ; DupClock>=0 ; DupClock--){

                String[] TempInst= new String[NoOfStr];
                System.out.print("\n\n=========================================");
                System.out.println("Instructions In Iteration "+itr+" is : ");
                System.out.println("Instruction : "+Inst[DupClock]);
                InstType[DupClock]=CheckInput(Inst[DupClock]);
                System.out.println("Instruction Type = "+InstType[DupClock]);
                System.out.println("Issuance Array : " + IssuanceArray[DupClock]);
                print(instructionStatusTable,ReservationStation,Load,Store);
                System.out.println("Load Table:");
                for (int n=0 ; n<3 ; n++)System.out.print(Load[n]+"     ");
                System.out.println("=========================================");

                if(InstType[DupClock]==1 || InstType[DupClock]==6){

                    if(IssuanceArray[DupClock]==-50 && instructionStatusTable[DupClock+1][1]=="-5"){
                        boolean Source_Available , Dest_Available;
                        int j=DupClock+1;
                        String issue=Integer.toString(j);
                        instructionStatusTable[j][1]=issue;
                        instructionStatusTable[j][0]=Inst[DupClock];
                        String DestReg = LoadDestFunction(Inst[DupClock]);
                        String SourceReg = LoadSourceFunction(Inst[DupClock]);
                        Dest_Available = CheckingDestRegister(DestReg);
                        if(InstType[DupClock]==1){
                            if (Load[0]=="0"){
                                Source_Available = CheckingLoadSourceRegister(SourceReg);
                                if(Source_Available==true && Dest_Available==true){
                                    time=StartTime(Inst[DupClock]);
                                    IssuanceArray[DupClock]=time;
                                    char Tempsource=DestReg.charAt(DestReg.length()-1);
                                    int TempIndex=Character.getNumericValue(Tempsource);
                                    Reg[TempIndex]=DestReg;
                                    Load[0]=SourceReg;
                                }
                                else{
                                    Waiting.add(Inst[DupClock]);
                                    WaitingInt.add(DupClock);
                                }
                            }

                            else if(Load[1]=="0"){
                                Source_Available = CheckingLoadSourceRegister(SourceReg);
                                if(Source_Available==true && Dest_Available==true){
                                    time=StartTime(Inst[DupClock]);
                                    IssuanceArray[DupClock]=time;
                                    char Tempsource=DestReg.charAt(DestReg.length()-1);
                                    int TempIndex=Character.getNumericValue(Tempsource);
                                    Reg[TempIndex]=DestReg;
                                    Load[1]=SourceReg;
                                }
                                else{
                                    Waiting.add(Inst[DupClock]);
                                    WaitingInt.add(DupClock);
                                }
                            }

                            else if(Load[2]=="0"){
                                Source_Available = CheckingLoadSourceRegister(SourceReg);
                                if(Source_Available==true && Dest_Available==true){
                                    System.out.println("Checking True Condition");
                                    time=StartTime(Inst[DupClock]);
                                    IssuanceArray[DupClock]=time;
                                    char Tempsource=DestReg.charAt(DestReg.length()-1);
                                    int TempIndex=Character.getNumericValue(Tempsource);
                                    Reg[TempIndex]=DestReg;
                                    Load[2]=SourceReg;
                                }
                                else{
                                    Waiting.add(Inst[DupClock]);
                                    WaitingInt.add(DupClock);
                                }
                            }
                        }
                        else if(InstType[DupClock]==6){
                            if (Store[0]=="0"){
                                Source_Available = CheckingStoreSourceRegister(SourceReg);
                                if(Source_Available==true && Dest_Available==true){
                                    time=StartTime(Inst[DupClock]);
                                    IssuanceArray[DupClock]=time;
                                    char Tempsource=DestReg.charAt(DestReg.length()-1);
                                    int TempIndex=Character.getNumericValue(Tempsource);
                                    Reg[TempIndex]=DestReg;
                                    Store[0]=SourceReg;
                                }
                                else{
                                    Waiting.add(Inst[DupClock]);
                                    WaitingInt.add(DupClock);
                                }
                            }

                            else if(Store[1]=="0"){
                                Source_Available = CheckingStoreSourceRegister(SourceReg);
                                if(Source_Available==true && Dest_Available==true){
                                    time=StartTime(Inst[DupClock]);
                                    IssuanceArray[DupClock]=time;
                                    char Tempsource=DestReg.charAt(DestReg.length()-1);
                                    int TempIndex=Character.getNumericValue(Tempsource);
                                    Reg[TempIndex]=DestReg;
                                    Store[1]=SourceReg;
                                }
                                else{
                                    Waiting.add(Inst[DupClock]);
                                    WaitingInt.add(DupClock);
                                }
                            }

                        }
                    }

                    else if(IssuanceArray[DupClock]==-50 && instructionStatusTable[DupClock+1][1]!="-5"){
                    }

                    else{
                        IssuanceArray[DupClock]=IssuanceArray[DupClock]-1;
                        if(IssuanceArray[DupClock]==-1){

                            String StrClock=Integer.toString(clock+1);
                            instructionStatusTable[DupClock+1][3]=StrClock;
                            String D=LoadDestFunction(Inst[DupClock]);
                            String S=LoadSourceFunction(Inst[DupClock]);

                            if(Load[0]==S)Load[0]="0";
                            else if(Load[1]==S)Load[1]="0";
                            else if(Load[2]==S)Load[2]="0";
                            else if(Store[0]==S)Store[0]="0";
                            else if(Store[1]==S)Store[1]="0";

                            char Tempsource=D.charAt(D.length()-1);
                            int TempIndex=Character.getNumericValue(Tempsource);
                            Reg[TempIndex]="0";
                        }
                        else if(IssuanceArray[DupClock]==0){
                            String StrClock=Integer.toString(clock+1);
                            instructionStatusTable[DupClock+1][2]=StrClock;
                        }
                        else{
                            continue;
                        }
                    }
                }

                else if(InstType[DupClock]==2 || InstType[DupClock]==3){

                    int j=clock+1;

                    if(IssuanceArray[DupClock]==-50 && instructionStatusTable[DupClock+1][1]=="-5"){

                        if(ReservationStation[1][2]=="No"){

                            String issue=Integer.toString(j);
                            instructionStatusTable[j][1]=issue;
                            String DestReg;
                            String[] Sources = new String[2];
                            boolean Source1_Available , Source2_Available;
                            boolean Dest_Available;

                            Sources = AddSourceFunction(Inst[DupClock]);
                            DestReg = AddDestFunction(Inst[DupClock]);
                            Source1_Available = CheckingAddSourceRegister(Sources[0]);
                            Source2_Available = CheckingAddSourceRegister(Sources[1]);
                            Dest_Available = CheckingDestRegister(DestReg);
                            instructionStatusTable[j][0]=Inst[DupClock];

                            if(Source1_Available==true && Source2_Available==true && Dest_Available==true){
                                time=StartTime(Inst[DupClock]);
                                IssuanceArray[DupClock]=time;
                                ReservationStation[1][2]="Yes";
                                char Tempsource=DestReg.charAt(DestReg.length()-1);
                                int TempIndex=Character.getNumericValue(Tempsource);
                                Reg[TempIndex]=DestReg;
                            }
                            else if(Source1_Available==true && Source2_Available==true && Dest_Available==false){
                                ReservationStation[1][3]=Sources[0];
                                ReservationStation[1][4]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==true && Source2_Available==false && Dest_Available==false){
                                ReservationStation[1][3]=Sources[0];
                                ReservationStation[1][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==false && Source2_Available==true && Dest_Available==false){
                                ReservationStation[1][4]=Sources[0];
                                ReservationStation[1][5]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==false && Source2_Available==false && Dest_Available==false){
                                ReservationStation[1][5]=Sources[0];
                                ReservationStation[1][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else{
                                Waiting.add(Inst[DupClock]);WaitingInt.add(DupClock);
                            }
                        }

                        else if(ReservationStation[2][2]=="No"){

                            String issue=Integer.toString(j);
                            instructionStatusTable[j][1]=issue;
                            String DestReg;
                            String[] Sources = new String[2];
                            boolean Source1_Available , Source2_Available;
                            boolean Dest_Available;

                            Sources = AddSourceFunction(Inst[DupClock]);
                            DestReg = AddDestFunction(Inst[DupClock]);
                            Source1_Available = CheckingAddSourceRegister(Sources[0]);
                            Source2_Available = CheckingAddSourceRegister(Sources[1]);
                            Dest_Available = CheckingDestRegister(DestReg);
                            instructionStatusTable[j][0]=Inst[DupClock];

                            if(Source1_Available==true && Source2_Available==true && Dest_Available==true){

                                time=StartTime(Inst[DupClock]);
                                IssuanceArray[DupClock]=time;
                                ReservationStation[2][2]="Yes";
                                char Tempsource=DestReg.charAt(DestReg.length()-1);
                                int TempIndex=Character.getNumericValue(Tempsource);
                                Reg[TempIndex]=DestReg;
                            }
                            else if(Source1_Available==true && Source2_Available==true && Dest_Available==false){
                                ReservationStation[2][3]=Sources[0];
                                ReservationStation[2][4]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);

                            }
                            else if(Source1_Available==true && Source2_Available==false && Dest_Available==false){
                                ReservationStation[2][3]=Sources[0];
                                ReservationStation[2][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);

                            }
                            else if(Source1_Available==false && Source2_Available==true && Dest_Available==false){
                                ReservationStation[2][4]=Sources[0];
                                ReservationStation[2][5]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==false && Source2_Available==false && Dest_Available==false){
                                ReservationStation[2][5]=Sources[0];
                                ReservationStation[2][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else{
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                        }
                        else if(ReservationStation[3][2]=="No"){

                            String issue=Integer.toString(DupClock);
                            instructionStatusTable[DupClock+1][1]=issue;
                            String DestReg;
                            String[] Sources = new String[2];
                            boolean Source1_Available , Source2_Available;
                            boolean Dest_Available;

                            Sources = AddSourceFunction(Inst[DupClock]);
                            DestReg = AddDestFunction(Inst[DupClock]);
                            Source1_Available = CheckingAddSourceRegister(Sources[0]);
                            Source2_Available = CheckingAddSourceRegister(Sources[1]);
                            Dest_Available = CheckingDestRegister(DestReg);

                            if(Source1_Available==true && Source2_Available==true && Dest_Available==true){
                                time=StartTime(Inst[DupClock]);
                                IssuanceArray[DupClock]=time;
                                ReservationStation[3][2]="Yes";
                                char Tempsource=DestReg.charAt(DestReg.length()-1);
                                int TempIndex=Character.getNumericValue(Tempsource);
                                Reg[TempIndex]=DestReg;
                            }
                            else if(Source1_Available==true && Source2_Available==true && Dest_Available==false){
                                ReservationStation[3][3]=Sources[0];
                                ReservationStation[3][4]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==true && Source2_Available==false && Dest_Available==false){
                                ReservationStation[3][3]=Sources[0];
                                ReservationStation[3][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==false && Source2_Available==true && Dest_Available==false){
                                ReservationStation[3][4]=Sources[0];
                                ReservationStation[3][5]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==false && Source2_Available==false && Dest_Available==false){
                                ReservationStation[3][5]=Sources[0];
                                ReservationStation[3][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);WaitingInt.add(DupClock);
                            }
                            else{
                                Waiting.add(Inst[DupClock]);WaitingInt.add(DupClock);

                            }

                        }
                    }
                    else if(IssuanceArray[DupClock]==-50 && instructionStatusTable[DupClock+1][1]!="-5"){

                    }
                    else{
                        IssuanceArray[DupClock]=IssuanceArray[DupClock]-1;

                        if(IssuanceArray[DupClock]==-1){

                            String[] S=new String[2];
                            String StrClock=Integer.toString(clock+1);
                            instructionStatusTable[DupClock+1][3]=StrClock;
                            String D=AddDestFunction(Inst[DupClock]);
                            S=AddSourceFunction(Inst[DupClock]);

                            if(ReservationStation[1][2]=="Yes")ReservationStation[1][2]="No";
                            else if(ReservationStation[2][2]=="Yes")ReservationStation[2][2]="No";
                            else if(ReservationStation[3][2]=="Yes")ReservationStation[3][2]="No";

                            char TempDest=D.charAt(D.length()-1);
                            int TempIndex=Character.getNumericValue(TempDest);
                            Reg[TempIndex]="0";
                        }

                        else if(IssuanceArray[DupClock]==0){

                            String StrClock=Integer.toString(clock+1);
                            instructionStatusTable[DupClock+1][2]=StrClock;
                        }

                        else{
                            continue;
                        }
                    }
                }

                else if(InstType[DupClock]==4 || InstType[DupClock]==5){

                    int j=clock+1;

                    if(IssuanceArray[DupClock]==-50 && instructionStatusTable[DupClock+1][1]=="-5"){

                        if(ReservationStation[4][2]=="No"){

                            String issue=Integer.toString(j);
                            instructionStatusTable[j][1]=issue;
                            String DestReg;
                            String[] Sources = new String[2];
                            boolean Source1_Available , Source2_Available;
                            boolean Dest_Available;

                            Sources = AddSourceFunction(Inst[DupClock]);
                            DestReg = AddDestFunction(Inst[DupClock]);
                            Source1_Available = CheckingAddSourceRegister(Sources[0]);
                            Source2_Available = CheckingAddSourceRegister(Sources[1]);
                            Dest_Available = CheckingDestRegister(DestReg);
                            instructionStatusTable[j][0]=Inst[DupClock];

                            if(Source1_Available==true && Source2_Available==true && Dest_Available==true){
                                time=StartTime(Inst[DupClock]);
                                IssuanceArray[DupClock]=time;
                                ReservationStation[4][2]="Yes";
                                char Tempsource=DestReg.charAt(DestReg.length()-1);
                                int TempIndex=Character.getNumericValue(Tempsource);
                                Reg[TempIndex]=DestReg;
                            }
                            else if(Source1_Available==true && Source2_Available==true && Dest_Available==false){
                                ReservationStation[4][3]=Sources[0];
                                ReservationStation[4][4]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);

                            }
                            else if(Source1_Available==true && Source2_Available==false && Dest_Available==false){
                                ReservationStation[4][3]=Sources[0];
                                ReservationStation[4][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==false && Source2_Available==true && Dest_Available==false){
                                ReservationStation[4][4]=Sources[0];
                                ReservationStation[4][5]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else if(Source1_Available==false && Source2_Available==false && Dest_Available==false){
                                ReservationStation[4][5]=Sources[0];
                                ReservationStation[4][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);
                            }
                            else{
                                Waiting.add(Inst[DupClock]);WaitingInt.add(DupClock);
                            }
                        }

                        else if(ReservationStation[5][2]=="No"){

                            String issue=Integer.toString(j);
                            instructionStatusTable[j][1]=issue;
                            String DestReg;
                            String[] Sources = new String[2];
                            boolean Source1_Available , Source2_Available;
                            boolean Dest_Available;

                            Sources = AddSourceFunction(Inst[DupClock]);
                            DestReg = AddDestFunction(Inst[DupClock]);
                            Source1_Available = CheckingAddSourceRegister(Sources[0]);
                            Source2_Available = CheckingAddSourceRegister(Sources[1]);
                            Dest_Available = CheckingDestRegister(DestReg);
                            instructionStatusTable[j][0]=Inst[DupClock];

                            if(Source1_Available==true && Source2_Available==true && Dest_Available==true){

                                time=StartTime(Inst[DupClock]);
                                IssuanceArray[DupClock]=time;
                                ReservationStation[5][2]="Yes";
                                char Tempsource=DestReg.charAt(DestReg.length()-1);
                                int TempIndex=Character.getNumericValue(Tempsource);
                                Reg[TempIndex]=DestReg;
                            }
                            else if(Source1_Available==true && Source2_Available==true && Dest_Available==false){
                                ReservationStation[5][3]=Sources[0];
                                ReservationStation[5][4]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);

                            }
                            else if(Source1_Available==true && Source2_Available==false && Dest_Available==false){
                                ReservationStation[5][3]=Sources[0];
                                ReservationStation[5][6]=Sources[1];
                                Waiting.add(Inst[DupClock]);
                                WaitingInt.add(DupClock);

                            }
                            else if(Source1_Available==false && Source2_Available==true && Dest_Available==false){
                                ReservationStation[5][4]=Sources[0];