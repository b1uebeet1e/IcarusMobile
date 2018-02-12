package mobile.icarus;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class MainFragment extends JFragment {
    private ExpandableListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        list=(ExpandableListView)view.findViewById(R.id.list);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        createView();

        return view;
    }

    @Override
    protected void form(){
        HashMap<String, ArrayList<ArrayList<String>>> pass =new HashMap<>();
        HashMap<String, ArrayList<ArrayList<String>>> not_pass =new HashMap<>();

        ArrayList<ArrayList<HashMap<String, String>>> allSubItemList = new ArrayList<>();

        ArrayList<HashMap<String, String>> subitemList1 = new ArrayList<>();
        ArrayList<HashMap<String, String>> subitemList2 = new ArrayList<>();
        HashMap<String, String> subItem;

        Elements trs=doc
                .select("div[id=tabs-1]")
                .select("table[id=analytic_grades]")
                .select("tbody")
                .select("tr");

        for(int i=0; i<trs.size(); i++){
            Elements tds=trs.get(i).select("td");
            if(clean(tds.get(6).text()).replace(" ","").equals("")) continue;

            if(pass.containsKey(clean(tds.get(2).text()))){
                ArrayList<String> tmp = new ArrayList<>();
                tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                pass.get(clean(tds.get(2).text())).add(tmp);
            }
            else {
                subItem = new HashMap<>();
                subItem.put("SUBITEM", clean(tds.get(2).text()));
                subItem.put("DETAIL", "Βαθμός: "+clean(tds.get(3).text()));
                subitemList2.add(subItem);

                if(not_pass.containsKey(clean(tds.get(2).text()))){
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                    tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                    not_pass.get(clean(tds.get(2).text())).add(tmp);

                    subitemList2.add(subItem);
                }

                else if(clean(tds.get(7).text()).replace(" ","").equals("Επιτυχία")){
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                    tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                    ArrayList<ArrayList<String>> tmp0 = new ArrayList<>();
                    tmp0.add(tmp);
                    pass.put(clean(tds.get(2).text()), tmp0);

                    subitemList1.add(subItem);

                }

                else{
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                    tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                    ArrayList<ArrayList<String>> tmp0 = new ArrayList<>();
                    tmp0.add(tmp);
                    not_pass.put(clean(tds.get(2).text()), tmp0);

                    subitemList2.add(subItem);
                }
            }
        }

        trs=doc
                .select("div[id=tabs-3]")
                .select("table[id=exetastiki_grades]")
                .select("tbody")
                .select("tr");

        for(int i=0; i<trs.size(); i++){
            Elements tds=trs.get(i).select("td");
            if(clean(tds.get(6).text()).replace(" ","").equals("")) continue;

            if(pass.containsKey(clean(tds.get(2).text()))){
                ArrayList<String> tmp = new ArrayList<>();
                tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                pass.get(clean(tds.get(2).text())).add(tmp);
            }
            else {
                subItem = new HashMap<>();
                subItem.put("SUBITEM", clean(tds.get(2).text()));
                subItem.put("DETAIL", "Βαθμός: "+clean(tds.get(3).text()));
                subitemList2.add(subItem);

                if(not_pass.containsKey(clean(tds.get(2).text()))){
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                    tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                    not_pass.get(clean(tds.get(2).text())).add(tmp);

                    subitemList2.add(subItem);
                }

                else if(clean(tds.get(7).text()).replace(" ","").equals("Επιτυχία")){
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                    tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                    ArrayList<ArrayList<String>> tmp0 = new ArrayList<>();
                    tmp0.add(tmp);
                    pass.put(clean(tds.get(2).text()), tmp0);

                    subitemList1.add(subItem);

                }

                else{
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add("Βαθμός: "+clean(tds.get(3).text()));
                    tmp.add("Ημερομηνία Εξέτασης: "+clean(tds.get(6).text()));
                    ArrayList<ArrayList<String>> tmp0 = new ArrayList<>();
                    tmp0.add(tmp);
                    not_pass.put(clean(tds.get(2).text()), tmp0);

                    subitemList2.add(subItem);
                }
            }
        }

        allSubItemList.add(subitemList1);
        allSubItemList.add(subitemList2);

        ArrayList<HashMap<String, String>> itemList = new ArrayList<>();
        HashMap<String, String> item = new HashMap<>();
        item.put("ITEM", "ΕΠΙΤΥΧΗ ΜΑΘΗΜΑΤΑ");
        item.put("DETAIL", "Αριθμός Δηλωμένων Επιτυχών Μαθημάτων: "+pass.size());
        itemList.add(item);
        item = new HashMap<>();
        item.put("ITEM", "ΑΝΕΠΙΤΥΧΗ ΜΑΘΗΜΑΤΑ");
        item.put("DETAIL", "Αριθμός Δηλωμένων Ανεπιτυχών Μαθημάτων: "+not_pass.size());
        itemList.add(item);

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getContext(),
                itemList,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{"ITEM", "DETAIL"},
                new int[]{android.R.id.text1, android.R.id.text2},
                allSubItemList,
                android.R.layout.simple_list_item_2,
                new String[]{"SUBITEM", "DETAIL"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        list.setAdapter(adapter);
    }
}
