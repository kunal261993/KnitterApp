package com.example.knitter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    ApiInterface apiService;
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    ProgressBar progressBar;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int totalPages =2;
    AlertDialog alertDialog;
    private int currentPage = 1;
    List<Post> posts;
    List<PostTable> postList;
    Boolean isConnect;
    SharedPreferences sh;
    IntentFilter intentFilter;
    boolean removeLoadingFooter=false;
    boolean loadFirstPage=false;
    int maxCurrentPage=1;
    boolean readDb=false;
    ConnectivityReceiver connectivityReceiver ;

    // TODO - Insert your API KEY here
    private final static String API_KEY = "GhCSeO6Pewu6pmudc1UZWFv0eBdFF4Yk2Gid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#4885ed"));
        actionBar.setBackgroundDrawable(colorDrawable);

        rv = (RecyclerView) findViewById(R.id.movies_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        adapter = new PaginationAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        apiService = ApiClient.getClient().create(ApiInterface.class);
        rv.setAdapter(adapter);
        sh = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Post postItem =  adapter.getItem(position);
                        String postId=postItem.getId().toString();
                        String userId=postItem.getUser_id().toString();
                        String title=postItem.getTitle();
                        String description=postItem.getBody();
                        Intent intent = new Intent(MainActivity.this, PostDetails.class);
                        intent.putExtra("postId", postId);
                        intent.putExtra("userId", userId);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isConnect = ConnectivityReceiver.isConnected();
                if(isConnect) {
                    if(readDb){
                        currentPage = sh.getInt("MAX_CURRENT_PAGE", 1);
                        readDb=false;
                    }
                    else
                    {
                        currentPage = sh.getInt("CURRENT_PAGE", 1);
                    }

                    totalPages = sh.getInt("TOTAL_PAGES", 2);
                    isLoading = true;
                    currentPage += 1;
                    // Mocking network delay for API call
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNextPage();
                        }
                    }, 1000);
                }
                else
                {
                    String test="No internet connection";
                    Toast.makeText(getApplicationContext(),test,Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public int getTotalPageCount() {

                return totalPages;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        // mocking network delay for API call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnect = ConnectivityReceiver.isConnected();
                if(isConnect) {
                    loadFirstPage();
                }
                else
                {
                    String test="No internet connection";
                    Toast.makeText(getApplicationContext(),test,Toast.LENGTH_SHORT).show();
                    ShowPost showPost=new ShowPost();
                    showPost.execute();
                }
            }
        }, 1000);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customdialog, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
        Button ok=(Button)alertDialog.findViewById(R.id.buttonOk);
        Button cancel=(Button)alertDialog.findViewById(R.id.buttonCancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(homeIntent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected)
    {
        isConnect = ConnectivityReceiver.isConnected();
        if(isConnect) {
            if (posts == null && loadFirstPage) {
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadFirstPage();
                    }
                }, 1000);
            }
        }
        else
        {
            if (removeLoadingFooter) {
                adapter.removeLoadingFooter();
                removeLoadingFooter = false;

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityReceiver = new ConnectivityReceiver();
        registerReceiver(connectivityReceiver, intentFilter);
        MyApplication.getInstance().setConnectivityListener(this);

    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

   private void loadFirstPage() {
           Call<PostResponse> call = apiService.getNewPosts(currentPage,"json",API_KEY);
           call.enqueue(new Callback<PostResponse>() {
               @Override
               public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                   currentPage = response.body().getMetaData().getCurrentPage();
                   totalPages = response.body().getMetaData().getPageCount();
                   SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                   SharedPreferences.Editor myEdit = sharedPreferences.edit();
                   myEdit.putInt("CURRENT_PAGE", currentPage);
                   myEdit.putInt("TOTAL_PAGES", totalPages);
                   myEdit.commit();
                   posts = response.body().getResult();
                   progressBar.setVisibility(View.GONE);
                   adapter.addAll(posts);
                   SavePost savePost=new SavePost();
                   savePost.execute();
                   if (currentPage <= totalPages) {
                       adapter.addLoadingFooter();
                       removeLoadingFooter=true;
                   }
                   else
                       isLastPage = true;
               }

               @Override
               public void onFailure(Call<PostResponse> call, Throwable t) {
                   // Log error here since request failed
                   Log.e(TAG, t.toString());
               }
           });
    }

    private void loadNextPage() {
            Call<PostResponse> call = apiService.getNewPosts(currentPage,"json",API_KEY);
            call.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    maxCurrentPage=sh.getInt("MAX_CURRENT_PAGE",1);
                    currentPage = response.body().getMetaData().getCurrentPage();
                    totalPages = response.body().getMetaData().getPageCount();
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putInt("CURRENT_PAGE", currentPage);
                    myEdit.putInt("TOTAL_PAGES", totalPages);
                    if(currentPage>maxCurrentPage) {
                        myEdit.putInt("MAX_CURRENT_PAGE", currentPage);
                    }
                    myEdit.commit();
                    posts = response.body().getResult();
                    if(removeLoadingFooter){
                    adapter.removeLoadingFooter();}
                    isLoading = false;
                    adapter.addAll(posts);
                    SavePost savePost=new SavePost();
                    savePost.execute();

                    if (currentPage != totalPages)
                    {
                        adapter.addLoadingFooter();
                        removeLoadingFooter=true;
                    }
                    else isLastPage = true;

                }
                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                }
            });
    }
    class SavePost extends AsyncTask<Void , Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            int i=0;
            for(i=0; i< posts.size(); i++)
            {
                PostTable postTable=new PostTable(posts.get(i).getId(), posts.get(i).getUser_id(), posts.get(i).getTitle(), posts.get(i).getBody());
                //adding to database
                try{
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(postTable);}
                catch(Exception e)
                {

                }
            }
            postList = DatabaseClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .taskDao()
                    .getPosts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class ShowPost extends AsyncTask<Void , Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

    if(posts ==null) {
    postList = DatabaseClient
            .getInstance(getApplicationContext())
            .getAppDatabase()
            .taskDao()
            .getPosts();
    if(postList.size()!=0) {
        int j = 0;
        posts = new ArrayList<>();

        for (j = 0; j < postList.size(); j++) {
            Post movie = new Post();
            movie.setId(postList.get(j).getPostId());
            movie.setUser_id(postList.get(j).getUserId());
            movie.setTitle(postList.get(j).getTitle());
            movie.setBody(postList.get(j).getDescription());
            posts.add(movie);
        }
    }
}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            if(postList.size()!=0) {
                adapter.addAll(posts);
                readDb=true;
            }
            else
            {
              loadFirstPage=true;
            }

        }
    }
}
