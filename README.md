# SwipePostcard
超级简单的滑动卡片实现<br>
继承自FrameLayout，使用ViewDragHelper，不到300行的代码量<br>

#Screenshot
* 有偏移
<br>
![](http://7xrqmj.com1.z0.glb.clouddn.com/qw2.gif)

* 无偏移
<br>
![](http://7xrqmj.com1.z0.glb.clouddn.com/qw1.gif)

#Usage
和RecyclerView的使用十分相似，你需要一个Adapter。
```java
public class PostcardAdapter extends SwipePostcard.Adapter {
    private final String TAG = PostcardAdapter.class.getSimpleName();
    private List<Bean> mData;

    public PostcardAdapter(Context context, List<Bean> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postcard, parent, false);
    }

    @Override
    public void bindView(View view, final int position) {
        Bean bean = mData.get(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
       
        //set view's behavior
    }

    @Override
    public int getItemCount() {
        return mData==null?0:mData.size();
    }

    static class ViewHolder {

        ViewHolder(View view) {
            //findview
        }
    }
}
```

然后setAdapter。
```java
 SwipePostcard postcard = (SwipePostcard) findViewById(R.id.postcards);
 PostcardAdapter adapter = new PostcardAdapter(this, data);
        if (postcard != null) {
            postcard.setAdapter(adapter);
            postcard.setMaxPostcardNum(3);  //存在的最大卡片数
            postcard.setOffsetY(67);        //偏移
            postcard.setMinDistance(200);   //使卡片消失的最小距离，0则为点一下就消失了
            postcard.setOnPostcardRunOutListener(new SwipePostcard.OnPostcardRunOutListener() {
                @Override
                public void onPostcardRunOut() {
                    //something to do
                }
            });

            postcard.setOnPostcardDismissListener(new SwipePostcard.OnPostcardDismissListener() {
                @Override
                public void onPostcardDismiss(int direction) {
                    if (direction == SwipePostcard.DIRECTION_LEFT) {
                        something todo
                    } else if (direction == SwipePostcard.DIRECTION_RIGHT) {
                        something todo
                    }
                }
            });
        }
```

#Thanks
@hongyangAndroid
<br>
他的博客 [Android ViewDragHelper完全解析 自定义ViewGroup神器](http://blog.csdn.net/lmj623565791/article/details/46858663)
写的很好，我从中得到的灵感，十分感谢。
