// -----------------------------IMPORT LIBRARY----------------------------------
import java.util.*;
import java.util.stream.Stream;
import java.io.*;

public class final {

// -----------------------------GENERATE VARIABLE----------------------------------

    private Vector<Vector<String>> Transaction;
    private Vector<Vector<String>> Pq;
    private Vector<Vector<String>> Utility;
    private Vector<Integer> uT;

    private Vector<String> alphabetic;
    private Vector<String> Profit;
    private int minUtility = 70;
    private double minCor = 0.52;

    public Text(){
        Transaction = new Vector<>();
        Pq = new Vector<>();
        Utility = new Vector<>();
        uT = new Vector<>();
        alphabetic = new Vector<>();
        Profit = new Vector<>();
    }


// -----------------------------MAIN FUNCTION----------------------------------

public void CoHui_Miner(){
       
    Vector<String> Ikeep = new Vector<>();
    Vector<String> Itrash = new Vector<>();
    HashMap<Vector<String>, Integer> SUP = Sup();
    Vector<Integer> TWU = Twu();
    
    for (int i=0 ;i<TWU.size();i++){
        if (TWU.get(i) >= minUtility){
            Ikeep.add(alphabetic.get(i));
        }
        else{
            Itrash.add(alphabetic.get(i));
        }
    }
    

    // System.out.println(TWU);

    for (int i = 0; i < Transaction.size(); i++) {
        Set<String> temp = new HashSet<>(Transaction.get(i));
        for (String trash: Itrash) {
            if (temp.contains(trash)) {
                int j = Transaction.get(i).indexOf(trash);
                Transaction.get(i).remove(j);
                Pq.get(i).remove(j);
                uT.set(i, uT.get(i) - Integer.parseInt(Utility.get(i).remove(j)));
            }
        }
        Sort(Transaction.get(i), Pq.get(i), Utility.get(i),SUP);
    }

    Vector<String> CoHUi = new Vector<>();
    for (String X : Ikeep){
        Vector<Vector<String>> T_X = new Vector<>();
        Vector<Vector<String>> U_X = new Vector<>();
        Vector<String> pruT_X = new Vector<>();
        Vector<Integer> uT_X = new Vector<>();
        

        if(U(X) >= minUtility){
            CoHUi.add(X);
            System.out.println(CoHUi);
            System.out.println(U(X));
        }
        Vector<String> check = new Vector<>();
        check.add(X);
        int RU = 0;
        for (int i = 0;i<Transaction.size();i++){
            int j = 0;
            
            if (IsContains(Transaction.get(i), check)){
                int uTemp = uT.get(i);
                while (j<Transaction.get(i).size() && compare(Transaction.get(i).get(j),check,SUP)){
                    uTemp = uTemp - Integer.parseInt(Utility.get(i).get(j));
                    j = j + 1;
                }

                if (j == Transaction.get(i).size() || compare1(Transaction.get(i).get(j),check,SUP))
                    continue;
                
                else if (j<Transaction.get(i).size()){
                    Vector<String> T = new Vector<>();
                    Vector<String> U = new Vector<>();
                    for(int e = j+1;e<Transaction.get(i).size();e++){
                        T.add(Transaction.get(i).get(e));
                        U.add(Utility.get(i).get(e));
                    }
                    T_X.add(T);
                    U_X.add(U);
                    pruT_X.add(Utility.get(i).get(j));
                    uT_X.add(uTemp);
                    RU += uTemp;
                }

            }
        }
        SearchCoHui(check,U(X),RU,T_X,pruT_X,uT_X,U_X,2,Ikeep,SUP);
    }
    
}


public void SearchCoHui(Vector<String> X,int U, int RU, Vector<Vector<String>> T_X, Vector<String> pruT_X,
 Vector<Integer> uT_X,Vector<Vector<String>> U_X,int k,Vector<String> Ikeep,HashMap<Vector<String>,Integer> SUP){
    if (k == Ikeep.size()){
        return;
    }
    Vector<Vector<String>> combo = new Vector<>();
    combo_recur(Ikeep, k, combo,new Vector<>());
    for (Vector<String> c: combo){

        Vector<Vector<String>> CoHui = new Vector<>();
        if (CHeck(X, c, k)){
            Vector<String> lastItem = (Vector<String>) c.clone();

            Vector<String> Xx = new Vector<>(X);
            if(!X.equals(lastItem)) {
                for (String item: lastItem) {
                    if (!Xx.contains(item)) {
                        Xx.add(item);
                    }
                }
            }
            
            Vector<Vector<String>> Trans = new Vector<>();
            Vector<Vector<String>> newutility = new Vector<>();
            Vector<String> pru_TX = new Vector<>();
            Vector<Integer> uT_TX = new Vector<>();
            int RU_ = 0;
            int SUP_ = 0;
            int UX_ = U;
            int ULA = U + RU;

            for (int i = 0; i < T_X.size(); i++) {
                int j = 0;
                int uTemp = uT_X.get(i);

                while (j < T_X.get(i).size() && compare(T_X.get(i).get(j), lastItem,SUP)) {
                    uTemp -= Integer.parseInt(U_X.get(i).get(j));
                    j++;
                }

                if (j == T_X.get(i).size() || compare1(T_X.get(i).get(j), lastItem,SUP)) {
                    UX_ -= Integer.parseInt(pruT_X.get(i));
                    ULA -= (Integer.parseInt(pruT_X.get(i)) + uT_X.get(i));

                    if (ULA < minUtility) {
                        return;
                    }
                    continue;
                }
                else {
                    UX_ += Integer.parseInt(U_X.get(i).get(j));
                    SUP_++;
                    

                    if (j < T_X.get(i).size()) {
                        Vector<String> T = new Vector<>();
                        Vector<String> Uti = new Vector<>();
                        for (int h = j+1; h < T_X.get(i).size(); h++) {
                            T.add(T_X.get(i).get(h));
                            Uti.add(U_X.get(i).get(h));

                        }
                        Trans.add(T);
                        newutility.add(Uti);
                        int u = Integer.parseInt(pruT_X.get(i))  + Integer.parseInt(U_X.get(i).get(j));
                        pru_TX.add(Integer.toString(u));
                        uT_TX.add(uTemp);
                        RU_ += uTemp;
                    }
                }
            }

            if(SUP_ >0){
                if (Kulc(Xx,SUP) >= minCor) {
                    if (UX_ >= minUtility) {
                        
                        CoHui.add(lastItem);
                        System.out.println(lastItem);
                        System.out.println(UX_);
                    }

                    if ((UX_ + RU_) >= minUtility) {
                        SearchCoHui(lastItem, UX_, RU_, Trans, pru_TX, uT_TX,newutility,k+1,Ikeep,SUP);
                    }
                }    
            }
        }
    }
    

}


// -----------------------------SUPPORT FUNCTION----------------------------------



    public void preprocessing(String path1, String path2) {
        try {
            File file = new File(path1);
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split("\\|");
                Transaction.add(new Vector<String>(Arrays.asList(parts[0].split(","))));
                Pq.add(new Vector<String>(Arrays.asList(parts[1].split(","))));
                Utility.add(new Vector<String>(Arrays.asList(parts[2].split(","))));
                
                uT.add(Integer.parseInt(parts[3]));
            }
            sc.close();

            file = new File(path2);
            sc = new Scanner(file);
            String[] K = sc.nextLine().split(",");
            for (String k: K ) {
                alphabetic.add(k);

            }
            
            String[] P = sc.nextLine().split(",");
            for (String p: P) {
                Profit.add(p);
            }
            
            sc.close();
            
        } 
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    

    private boolean IsContains(Vector<String> parent, Vector<String> child) {
        Set<String> p = new HashSet<>(parent);
        return p.containsAll(child);
    }

    public HashMap<Vector<String>,Integer> Sup() {
        HashMap<Vector<String>,Integer> SUP = new HashMap<>();
        for (int i = 0;i<alphabetic.size();i++){
            int count = 0;
            Vector<String> ans = new Vector<>();
            ans.add(alphabetic.get(i));
            for (Vector<String> tr: Transaction) {
                if (IsContains(tr, ans)) {
                    count += 1;
                }
                SUP.put(ans, count);
            }
        }
        return SUP;
    }

    private int ChangetoInt(String s) {
        return (int)(s.charAt(0));
    }

    public int U(String charac){
        int sum = 0;
        for (int i = 0;i<Transaction.size();i++){
            for (int j = 0;j<Transaction.get(i).size();j++){
                if (Transaction.get(i).get(j).equals(charac)){
                    sum = sum + Integer.parseInt(Utility.get(i).get(j));
                }
            }
        }
        return sum;
    }
    

    private Vector<Integer> Twu(){
        Vector<Integer> TWU = new Vector<>();
        for (int i=0;i<alphabetic.size();i++){
            int sum = 0;
            Vector<String> ans = new Vector<>();
            ans.add(alphabetic.get(i));
            for (int trans = 0; trans < Transaction.size() ;trans++){
                if(IsContains(Transaction.get(trans), ans)){
                    sum += (uT.get(trans));
                }
            }
            TWU.add(sum);

        }
        return TWU;
    }

    public void Sort(Vector<String> Trans, Vector<String> Pq, Vector<String> U,HashMap<Vector<String>, Integer> SUP){
        for (int i = 0;i<Trans.size()-1;i++){

            for(int j = i+1;j<Trans.size();j++){
                Vector<String> ans1 = new Vector<>();
                Vector<String> ans2 = new Vector<>();
                String temp1 = Trans.get(i);
                String temp2 = Trans.get(j);
                ans1.add(temp1);
                ans2.add(temp2);
                if(SUP.get(ans2) < SUP.get(ans1)){
                    Collections.swap(Trans, i, j);
                    Collections.swap(Pq, i, j);
                    Collections.swap(U, i, j);
                }

                else if(SUP.get(ans2) == SUP.get(ans1)){
                    if(ChangetoInt(Trans.get(i)) > ChangetoInt(Trans.get(i))){
                        Collections.swap(Trans, i, j);
                        Collections.swap(Pq, i, j);
                        Collections.swap(U, i, j);
                    }
                }
            }
        }
    }

    public boolean compare(String x, Vector<String> X,HashMap<Vector<String>,Integer> SUP){
        
        int count = 0;
        Vector<String> check = new Vector<>();
        check.add(x);
        for(String element : X){
            Vector<String> check1 = new Vector<>();
            check1.add(element);
            if (SUP.get(check) < SUP.get(check1) || (SUP.get(check) == SUP.get(check1) && ChangetoInt(x) < ChangetoInt(element))){
                count += 1;
            }
        }
        if (count == X.size()){
            return true;
        }
        return false;
    }

    public boolean compare1(String x, Vector<String> X,HashMap<Vector<String>,Integer> SUP){
        
        int count = 0;
        Vector<String> check = new Vector<>();
        check.add(x);
        for(String element : X){
            Vector<String> check1 = new Vector<>();
            check1.add(element);
            if (SUP.get(check) > SUP.get(check1) || (SUP.get(check) == SUP.get(check1) && ChangetoInt(x) > ChangetoInt(element))){
                count += 1;
            }
        }
        if (count == X.size()){
            return true;
        }
        return false;
    }


    private String ChangetoCHar(int n) {
        return "" + (char) n;
    }

    private void combo_recur(Vector<String> S, int k, Vector<Vector<String>> result, Vector<String> x) {
        if (x.size() < k)  {
            int a = (x.size() == 0) ? ChangetoInt(S.get(0)) : ChangetoInt(x.get(x.size() - 1)) + 1;
            int b = ChangetoInt(S.get(S.size() - 1)) + 1;
            Vector<Integer> candidates = new Vector<>();
            for (int i = a; i < b; i++) {
                candidates.add(i);
            }

            for (Integer c: candidates) {
                Vector<String> t = (Vector<String>) x.clone();
                t.add(ChangetoCHar(c));
                if (t.size() == k) {
                    result.add(t);
                }
                combo_recur(S, k, result, t);
            }
        }
    }


    private double Kulc(Vector<String> Item, HashMap<Vector<String>,Integer> SUP ) {
        double counter = 0;
        for (Vector<String> T: Transaction) {
            if (IsContains(T, Item)) {
                counter++;
            }
        }
        
        double result = 0;
        for (String x: Item) {
            Vector<String> temp = new Vector<>();
            temp.add(x);
            result += (double) (counter / SUP.get(temp));
        }
        
        return (double) result / Item.size();
    }


    private boolean CHeck(Vector<String> s1, Vector<String> s2, int k) {
        if (k == 1) return s1.equals(s2);
        
        for (int i = 0; i < k - 1; i++) {
            if (!s1.get(i).equals(s2.get(i))) {
                return false;
            }
        }
        return true;       
    }


    public static void main(String[] args) {
        Text t = new Text();
        t.preprocessing("520H0675_520H0464_52000808/data.txt", "520H0675_520H0464_52000808/profit.txt");
        // t.Display();
        t.CoHui_Miner();
    }

}
