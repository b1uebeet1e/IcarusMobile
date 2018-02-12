package mobile.icarus;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Map;

public class JFragment extends Fragment {
    protected Document doc;
    protected SwipeRefreshLayout swipe;

    protected void createView() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });

        doc=((TabActivity)getActivity()).doc;
        form();
    }

    protected String clean(String str){
        return str
                .replace("<td>", "")
                .replace("</td>", "");
    }

    protected void form(){}

    protected void update(){
        final StringBuilder str=new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    doc=Jsoup
                            .connect("https://icarus-icsd.aegean.gr/")
                            .cookies((Map<String, String>)getArguments().getSerializable("cookies"))
                            .post();

                    if(doc.select("div[id=header_login]").select("u").html().length()==0) str.append("It seems that your credentials are no longer valid :(");

                }catch (Exception e){
                    str.append("Network Error");
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(str.length()>0){
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Unexpected Error")
                                    .setMessage(str.toString())
                                    .setCancelable(false)
                                    .setPositiveButton("Exit App",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ((TabActivity)getActivity()).logout();
                                                    getActivity().finish();
                                                }
                                            })
                                    .setNegativeButton("Login again",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ((TabActivity)getActivity()).logout();
                                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                                    getActivity().finish();
                                                }
                                            })
                                    .setNeutralButton("Ignore",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            })
                                    .show();
                        }
                        doc=((TabActivity)getActivity()).doc;
                        form();
                        ((TabActivity)getActivity()).refresh();
                        swipe.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}

