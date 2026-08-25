import java.util.Scanner;

public class CS2113_Bot {

    public static void main(String[] args) {
        String line;
        Task[] my_List = new Task[100];
        int list_Count = 0;

        Scanner in = new Scanner(System.in);

        String banner = "_    _ ______ _      _      ____   __          __  {_} _____  _      _____  \n"
                + "| |  | |  ____| |    | |    / __ \\  \\ \\        / / | |  __ \\| |    |  __ \\ \n"
                + "| |__| | |__  | |    | |   | |  | |  \\ \\  /\\  / /  | | |__) | |    | |  | |\n"
                + "|  __  |  __| | |    | |   | |  | |   \\ \\/  \\/ /   | |  _  /| |    | |  | |\n"
                + "| |  | | |____| |____| |____| |__| |    \\  /\\  /    | | | \\ \\| |____| |__| |\n"
                + "|_|  |_|______|______|______|\\____/      \\/  \\/     |_| |_| \\_\\______|_____/ ";
        System.out.println("__________________________________");
        System.out.println(banner);
        System.out.println("\nHello! I'm CS2113_Bot.");
        System.out.println("What can I do for you?");
        System.out.println("__________________________________");
        line = in.nextLine();
        while(!(line.toLowerCase()).equals("bye")){
            System.out.println("__________________________________");
            if(line.toLowerCase().equals("list")){
                System.out.println("Here are the tasks in your list:");
                for(int i=0;i<list_Count;i++){
                    System.out.println((i+1) + ".[" + my_List[i].getStatusIcon() + "] " + my_List[i].description);
                }
                System.out.println("__________________________________");
            }
            else if (line.toLowerCase().contains("mark")) {
                String[] words = line.split(" ");
                int mark_Index = Integer.parseInt(words[1]) - 1;
                if(words[0].equals("mark")){
                    my_List[mark_Index].setDone(true);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(("[" + my_List[mark_Index].getStatusIcon() + "] " + my_List[mark_Index].description));
                    System.out.println("__________________________________");
                }
                else{
                    my_List[mark_Index].setDone(false);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(("[" + my_List[mark_Index].getStatusIcon() + "] " + my_List[mark_Index].description));
                    System.out.println("__________________________________");
                }

            }
            else{
                System.out.println(line);
                System.out.println("__________________________________");
                my_List[list_Count++] = new Task(line);
            }
            line = in.nextLine();
        }

        System.out.println("__________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("__________________________________");
    }
}
