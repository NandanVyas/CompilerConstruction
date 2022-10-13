import java.util.*;

/**
 * Nandan Vyas
 */
public class A1 {

    public static List<Integer> epsilonClosure(int[][] graph, int element) //NULL CLOSURE OF INITIAL STATE
    {
        List<Integer> closure = new ArrayList<>();
        closure.add(element);
        // INCLUDES ONLY NULL TRANSITIONS HENCE /a, b/ NOT CONSIDERED
        if(graph[element][2]>-1) closure.addAll(epsilonClosure(graph, graph[element][2])); 
        if(graph[element][3]>-1) closure.addAll(epsilonClosure(graph, graph[element][3]));

        return closure;
    }

    public static List<Integer> check_A(int[][] graph, List<Integer> state) // CHECKS ALL ELEMENTS OF THE STATE ON A
    {
        List<Integer> a = new ArrayList<>();
        for(int i : state)
        {
            if(graph[i][0]>-1)
            {
                a.add(graph[i][0]);
            }
        }
        return a; // RETURNS THE READING OF A ON THE STATE
    }

    public static List<Integer> check_B(int[][] graph, List<Integer> state) // CHECKS ALL ELEMENTS OF THE STATE ON B
    {
        List<Integer> b = new ArrayList<>();
        for(int i : state)
        {
            if(graph[i][1]>-1)
            {
                b.add(graph[i][1]);
            }
        }
        return b; //  RETURNS THE READING OF B ON THE STATE
    }

    // NFA --> DFA
    public static List<List<Integer>> getStatesSubset(int[][] graph) //TO GENERATE ALL THE UNIQUE STATES USING SUBSET CONSTRUCTION
    {
        List<List<Integer>> states = new ArrayList<>();

        List<Integer> start = epsilonClosure(graph, 0);
        start.sort(null); // SORTs THE STATES (null --> natural sort / increasing order)

        List<List<Integer>> stack = new ArrayList<>(); // ORDINARY LIST ONLY NAME IS STACK 
        stack.add(start);
        while(!stack.isEmpty()) // TO CONTINUE ITERATION UNTIL NO NEW STATES 
        {
            List<Integer> state = stack.get(0);
            stack.remove(0);
            states.add(state);
            List<Integer> a = check_A(graph, state); 
            List<Integer> b = check_B(graph, state);
            List<Integer> eca=new ArrayList<>(),ecb=new ArrayList<>();// EPISILION CLOSURE OF STATES
            for(int i : a)
            {
                eca.addAll(epsilonClosure(graph, i));
            }
            for(int i : b)
            {
                ecb.addAll(epsilonClosure(graph, i));
            }
            // System.out.println(eca);
            eca.sort(null);
            ecb.sort(null);
            if(!states.contains(eca)) stack.add(eca); // TO CHECK IF IT IS ALREAY PRESENT (ONLY UNIQUE STATES)
            if(!states.contains(ecb)) stack.add(ecb);
        }
        // System.out.println("States using Subset Construction : \n"+states+"\n");
        return states;
    }


    public static Map<String,String> minimizeDFA(int[][] graph)
    {
        List<List<Integer>> states =  getStatesSubset(graph);
        Map<List<Integer>,Character> map = new LinkedHashMap<>(); // MAP TO STORE STATE AND ITS MINIMISED NAME
        int index=0;
        for(List<Integer> i : states)
        {
            map.put(i,(char)('A'+index++)); // FIRST STATE --> 'A'  , SECOND --> 'B'...
        }
        // System.out.println(map);
        Map<String,String> dfa = new LinkedHashMap<>();
        for(List<Integer> state : states)
        {
            List<Integer> a = check_A(graph, state);
            List<Integer> b = check_B(graph, state);
            List<Integer> eca=new ArrayList<>(),ecb=new ArrayList<>();
            for(int i : a)
            {
                eca.addAll(epsilonClosure(graph, i));
            }
            for(int i : b)
            {
                ecb.addAll(epsilonClosure(graph, i));
            }
            // System.out.println(eca);
            eca.sort(null);
            ecb.sort(null);

            dfa.put(map.get(state)+"", map.get(eca)+""+map.get(ecb)); // THE STATE --> STATE ON READING A + STATE ON READING B

        }
        // System.out.println("Minimized DFA :\n"+dfa+"\n");
        // prettyPrintDFA(dfa);
        return dfa;
    }

    public static void prettyPrintDFA(Map<String,String> dfa) // JUST TO PRINT THE DFA
    {
        System.out.println("State  --->   a\t\tb\n");
        for (Map.Entry<String,String> entry : dfa.entrySet()) 
        {
            System.out.println("  "+entry.getKey() +"    --->   "+ entry.getValue().charAt(0)+"\t\t"+entry.getValue().charAt(1));   
        } 
    }

    public static void validateString(String s, int[][] graph) // TO VALIDATE THE ENTERED STRING
    {
        System.out.print("Checking - "+s+" : ");
        Map<String,String> dfa = minimizeDFA(graph);
        char current = 'A'; // THERE CAN BE ONE OR MANY STARTING STATES BUT IN OUR CASE ITS ONLY ONE --> 'A'
        for(int i=0;i<s.length();i++)
        {
            char ch = s.charAt(i);
            if(ch == 'a')
                current = dfa.get(current+"").charAt(0);
            else if(ch=='b')
                current = dfa.get(current+"").charAt(1);
            else 
            {
                System.out.println("Invalid String \n");
                return; 
            }
        }
        // ENDING STATES CAN BE ONE OR MANY IN OUR CASE ITS JUST ONE --> 'E' ( '10' ONLY APPEARS IN 'E' )
        if(current == 'E') System.out.println("Valid String \n");
        else System.out.println("Invalid String \n");
       
    }

    

    public static void main(String[] args) 
    {
        // CURRENT(INDEX) STATE TO STATE ON GETTING --> [a, b, E1, E2]
        int[][] graph = new int[][] {
            {-1, -1, 1, 7},
            {-1, -1, 2, 4},
            {3, -1, -1, -1},
            {-1, -1, 6, -1},
            {-1, 5, -1, -1},
            {-1, -1, 6, -1},
            {-1, -1, 1, 7},
            {8, -1, -1, -1},
            {-1, 9, -1, -1},
            {-1, 10, -1, -1},
            {-1, -1, -1, -1}
        };
        //
        // getStates(graph);
        System.out.println("States using Subset Construction : \n"+getStatesSubset(graph)+"\n");
        System.out.println("Minimised DFA : ");
        prettyPrintDFA(minimizeDFA(graph));
        System.out.println(" ");
        validateString("aaaabbbbabb", graph);
        validateString("aaaabbbbabab", graph);
        // System.out.println("Success");
        
    }

}




// public static List<List<Integer>> getStates(int[][] graph)   
    /*
    public static void getStates(int[][] graph) 
    {
        List<List<Integer>> states = new ArrayList<>();
        List<Integer> start = epsilonClosure(graph, 0);
        start.sort(null);
        states.add(start);
        List<Integer> a = check_A(graph, states.get(0));
        List<Integer> b = check_B(graph, states.get(0));
        List<Integer> eca=new ArrayList<>(),ecb=new ArrayList<>();
        for(int i : a)
        {
            eca.addAll(epsilonClosure(graph, i));
        }
        for(int i : b)
        {
            ecb.addAll(epsilonClosure(graph, i));
        }
        // System.out.println(eca);
        eca.sort(null);
        ecb.sort(null);
        if(!states.contains(eca)) states.add(eca);
        if(!states.contains(ecb)) states.add(ecb);
        // System.out.println(eca);
        System.out.println("States : \n" + states +"\n");
        // return states;
    } 
    */
