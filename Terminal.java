import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
public class Terminal extends Commands {
    private Parser parser;

    public Terminal() {
        this.parser = new Parser();
    }

    public void run() {
        while (true) {
            System.out.print("Enter a command: ");
            String input = System.console().readLine();

            if (input.equals("exit")) {
                break;
            }

            if (parser.parse(input)) {
                int index = Arrays.asList(parser.getArgs()).indexOf(">");
                int index2 = Arrays.asList(parser.getArgs()).indexOf(">>");
                if(index!=-1){
                  String [] copy = Arrays.copyOfRange(parser.getArgs(), index+1, parser.getArgs().length);
                  String [] main = Arrays.copyOfRange(parser.getArgs(), 0, index);
                  System.out.println(copy[0]);
                  String commandAns = chooseCommandAction(main);
                  operator1Command(commandAns , copy);
                  
                }
                else if(index2!=-1){
                  String [] copy = Arrays.copyOfRange(parser.getArgs(), index2+1, parser.getArgs().length);
                  String [] main = Arrays.copyOfRange(parser.getArgs(), 0, index2);
                  System.out.println(copy[0]);
                  String commandAns = chooseCommandAction(main);
                  operator2Command(commandAns , copy);
                }
                else{String commandAns = chooseCommandAction(parser.getArgs()); System.out.println(commandAns);}
            }
            
            else {
                System.out.println("Invalid command.");
            }
        }
    }


    public void operator1Command (String commandAns , String[] copy){
         if(copy.length==1){
           try{
             FileWriter file = new FileWriter(copy[0]);

             file.write(commandAns);

             file.close();

           }
           catch(IOException  e){
               e.printStackTrace();
           }
         }
         else{
            System.out.println("Invalid usage ! Usage : command > <filename>");
         }
    }

    public void operator2Command (String commandAns , String[] copy){
         if(copy.length==1){
           try{
               File file = new File(copy[0]);
               if((file.exists())){
                   FileWriter file2 = new FileWriter(copy[0]);
                   file2.write(commandAns);
                   file2.close();
               }
               else{
                System.out.println("No Such File Exists!");
               }
             }

           catch(IOException  e){
               e.printStackTrace();
           }
         }
         else{
            System.out.println("Invalid usage ! Usage : command >> <filename>");
         }
    }

    public String chooseCommandAction(String [] args ) {
        String commandName = parser.getCommandName();
        //String[] args = parser.getArgs();
        

        switch(commandName){

            case "echo"  :
             if(args.length==1)  return args[0];
             else  return ("Invalid usage. Usage: echo <text>"); 
            
            case "pwd":
             return(System.getProperty("user.dir"));
            

            case "cd":
             return cdCommand(args);
            
            
            case "ls":
              return lsCommand(args);
              
            
            case "touch":
             return touchCommand(parser.getArgs()[0]);
            

            case "cp":

              if (args.length == 2){
               cpCommand(args[0], args[1]);} 

              else if (args.length == 3 && args[0].equals("-r")){
                cprCommand(new File(args[1]), new File(args[2]));}
                
              else {
                System.out.println("Invalid usage. Usage: touch <file_name>");}

            case "rm":
             return rmCommand(args);
            

            case "cat":
             return catCommand(args);
             

            case "wc":
             return wcCommand(args);
            

            default :
             return ("Command Not Found !"+commandName);
            
        }
    }
}
