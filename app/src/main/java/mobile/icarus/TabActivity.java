package mobile.icarus;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.github.clans.fab.FloatingActionButton;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TabActivity extends AppCompatActivity {

    public Document doc;
    private Map<String, String> cookies;
    private TabLayout tabLayout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton r_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        doc= Jsoup.parse(getIntent().getExtras().getString("doc"));
        cookies=(Map<String, String>)getIntent().getSerializableExtra("cookies");

        //Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        r_button = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);

        r_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(
                        0,
                        360,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                );

                rotate.setDuration(500);
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setRepeatMode(Animation.INFINITE);
                rotate.setInterpolator(new LinearInterpolator());

                r_button.startAnimation(rotate);
                update();
            }
        });
        findViewById(R.id.material_design_floating_action_menu_item2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(TabActivity.this, LoginActivity.class));
                finish();
            }
        });
        findViewById(R.id.material_design_floating_action_menu_item3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(TabActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exiting App")
                        .setMessage("Are you sure you want to exit Icarus Mobile")
                        .setCancelable(true)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                        .setNeutralButton("Login as another user",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        logout();
                                        startActivity(new Intent(TabActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                })
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(TabActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting App")
                .setMessage("Are you sure you want to exit Icarus Mobile")
                .setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                .setNeutralButton("Login as another user",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout();
                                startActivity(new Intent(TabActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                .show();
    }

    private void update(){
        final StringBuilder str=new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    doc=Jsoup
                            .connect("https://icarus-icsd.aegean.gr/")
                            .cookies(cookies)
                            .post();

                    if(doc.select("div[id=header_login]").select("u").html().length()==0) str.append("It seems that your credentials are no longer valid :(");

                }catch (Exception e){
                    str.append("Network Error");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(str.length()>0){
                            new AlertDialog.Builder(TabActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Unexpected Error")
                                    .setMessage(str.toString())
                                    .setCancelable(false)
                                    .setPositiveButton("Exit App",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            })
                                    .setNegativeButton("Login again",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    logout();
                                                    startActivity(new Intent(TabActivity.this, LoginActivity.class));
                                                    finish();
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
                        refresh();
                        r_button.clearAnimation();
                    }
                });
            }
        }).start();
    }

    public void refresh(){
        int tmp = mViewPager.getCurrentItem();
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(tmp);
    }

    public void logout(){
        try {
            Jsoup
                    .connect("https://icarus-icsd.aegean.gr/logout.php")
                    .referrer("https://icarus-icsd.aegean.gr/student_main.php")
                    .cookies(cookies)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 3;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putSerializable("cookies", (HashMap<String, String>)cookies);
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new MainFragment();
                    fragment.setArguments(args);
                    return fragment;

                case 1:
                    fragment = new SecondFragment();
                    fragment.setArguments(args);
                    return fragment;

                case 2:
                    fragment = new ThirdFragment();
                    fragment.setArguments(args);
                    return fragment;

                default:
                    fragment = new MainFragment();
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ΑΝΑΛΥΤΙΚΗ ΒΑΘΜΟΛΟΓΙΑ";
                case 1:
                    return "ΕΞΕΤΑΣΤΙΚΗ";
                case 2:
                    return "ΓΕΝΙΚΑ";
            }
            return null;
        }
    }
}