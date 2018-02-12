package mobile.icarus;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.select.Elements;

public class ThirdFragment extends JFragment {
    private TextView name, am, state, succeeded, succeeded_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        name = (TextView) view.findViewById(R.id.name);
        am = (TextView) view.findViewById(R.id.am);
        state = (TextView) view.findViewById(R.id.state);
        succeeded = (TextView) view.findViewById(R.id.succeeded);
        succeeded_all = (TextView) view.findViewById(R.id.succeeded_all);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        createView();

        return view;
    }

    @Override
    protected void form(){
        name.setText(doc.body()
                .select("div[id=header_login]")
                .select("u")
                .html()
        );

        am.setText(doc.body()
                .select("div[id=tabs-1]")
                .select("h2")
                .html()
                .substring(16,27)
        );

        state.setText(doc
                .body()
                .select("div[id=tabs-1]")
                .select("h2").html().substring(43)
        );

        int succ=0;

        Elements trs=doc
                .select("div[id=tabs-1]")
                .select("table[id=analytic_grades]")
                .select("tbody")
                .select("tr");

        for(int i=0; i<trs.size(); i++){
            Elements tds=trs.get(i).select("td");
            if(clean(tds.get(7).text()).replace(" ","").equals("Επιτυχία")){
                succ++;
            }
        }

        trs=doc
                .select("div[id=tabs-3]")
                .select("table[id=exetastiki_grades]")
                .select("tbody")
                .select("tr");

        for(int i=0; i<trs.size(); i++){
            Elements tds=trs.get(i).select("td");
            if(clean(tds.get(7).text()).replace(" ","").equals("Επιτυχία")){
                succ++;
            }
        }

        succeeded_all.setText(Integer.toString(succ));
        succeeded.setText(Integer.toString(doc
                        .select("div[id=tabs-2]")
                        .select("table[id=succeeded_grades]")
                        .select("tbody")
                        .select("tr")
                        .size()
                )
        );
    }
}