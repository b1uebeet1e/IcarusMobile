package mobile.icarus;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class SecondFragment extends JFragment {
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        list=(ListView)view.findViewById(R.id.list);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        createView();

        return view;
    }

    @Override
    protected void form(){
        Elements trs=doc
                .select("div[id=tabs-3]")
                .select("table[id=exetastiki_grades]")
                .select("tbody")
                .select("tr");

        ArrayList<HashMap<String,String>> magic_ls = new ArrayList<HashMap<String,String>>();
        for(int i=0; i<trs.size(); i++){
            Elements tds=trs.get(i).select("td");
            HashMap<String,String> item = new HashMap<String,String>();
            item.put("subject", clean(tds.get(2).text()));
            if(clean(tds.get(3).text()).length()==0) item.put("grade", "Βαθμός: Μη Διαθέσιμος :(");
            else item.put("grade", "Βαθμός: "+clean(tds.get(3).text()));
            magic_ls.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(
                getContext(),
                magic_ls,
                android.R.layout.simple_list_item_2,
                new String[] { "subject","grade" },
                new int[] {android.R.id.text1, android.R.id.text2}
        );

        list.setAdapter(adapter);
    }
}

