Knitter App:

Progress :

1) Able to display all posts using Retrofit and Pagination.
2) Able to save post data in Room Db.
3) Able to show saved data when internet is not available. 

Unable to show comments due to time limitation of the task.

Classes Used :
1)SplashActivity - Used to show splash screen.
2)MainActivity - Used to show posts.
3)Post Details - Used to show post details.
4)Post - Model used to serialize JSON for post details.
5)Post Response - Model used to serialize JSON for response details.
6)MyApplication - Used to check internet connectivity.
7)ConnectivityReceiver - Used to check internet connectivity.
8)PaginationAdapter - Used to handle pagination
9)PaginationScrollListener - Used to handle pagination scroll listening.
10)RecyclerItemClickListener - Used to handle recycler view item click.
11)ApiClient - Contains Baseurl and also url builder
12)ApiInterface - Contains Endpoints.
13)PostTable - Entity class for DB.
14)PostDao - Dao interface for DB.
15)AppDatabase - Database abstract class extending RoomDatabase
16)DatabaseClient - Used to create DB.

Note:- Please enter api key in MainActivity.java.

