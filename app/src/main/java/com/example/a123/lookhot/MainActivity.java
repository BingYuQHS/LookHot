package com.example.a123.lookhot;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private LinearLayout linearLayout;
    private ImageView rollImage;
    private ListView hoList;
    private int picOder=0;      //根据此值设置轮播图片应该播放哪张图片
    List<HotNews> hotNews;
    //    下面五个参数是实现轮播图片的
    private ViewPager vp;
    private  int[] imas;    //图片数组
    private LinearLayout ll;    //小圆点布局
    private ArrayList<ImageView> imaList;
    private Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //         实现轮播
        initViews();             //初始化布局
        initAdapter();          //适配器
        flag=true;
//                实现自动轮播
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //利用MainActivity.this获取当前context环境
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            vp.setCurrentItem(vp.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();

//        addNewsData();                              //向“新闻”数据库中添加娱乐、财经、军事数据
        getNewsList();                              //获取“新闻”数据库中数据
        hoList=findViewById(R.id.hot_list);
        NewsAdaptor adaptor=new NewsAdaptor(this,hotNews); //获得适配器
        hoList.setAdapter(adaptor);                                   //添加适配器
        hoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//点击新闻item跳转至相应详情页面
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("clickitem");
                ContentResolver cr = MainActivity.this.getContentResolver();
                Uri uri = Uri.parse
                        ("content://com.example.a123.lookhot.news/hotlook_table");
                Cursor c = cr.query(uri,null,null,null,null);

                int scId=0;
                try {
                    c.moveToPosition(position);// cursor1移动到点击的当前行
                    scId = c.getInt(0); // 获得这一行的在数据库中的id
                } catch (Exception e) {
// TODO: handle exception
                    e.printStackTrace();
                }
                c.close();
//                跳转新闻详情页面
                Intent intent = new Intent();
                System.out.println("intent");
                intent.putExtra("newsId",position);
//                intent.putExtra("newsId",scId-1);
                System.out.println("position："+position);
                System.out.println("mainactivityid："+scId);
                intent.setClass(MainActivity.this,NewsDetailActivity.class);
                startActivity(intent);
            }
        });

        //清空数据库中数据
//        deleteData();
    }

    //获取“新闻”数据库中数据
    public  void getNewsList(){
        hotNews = new ArrayList<HotNews>();
        ContentResolver cr = MainActivity.this.getContentResolver();
        Uri uri = Uri.parse
                ("content://com.example.a123.lookhot.news/hotlook_table");
        Cursor c = cr.query(uri,null,null,null,null);
        while (c.moveToNext()){
            int _id=c.getInt(c.getColumnIndex("_id"));
            String news_title=c.getString(c.getColumnIndex("news_title"));
            int comment_count=c.getInt(c.getColumnIndex("comment_count"));
            String news_type=c.getString(c.getColumnIndex("news_type"));
            String str=c.getString(c.getColumnIndex("publish_date"));
            System.out.println("出版时间："+str);

            HotNews hot = new HotNews(_id,news_title,comment_count,news_type);
            hotNews.add(hot);
        }
        c.close();
        System.out.println("获取数据成功！");
    }

    //    向数据库中添加数据
    public void addNewsData() {
        ContentResolver cr = MainActivity.this.getContentResolver();
        Uri uri = Uri.parse
                ("content://com.example.a123.lookhot.news/hotlook_table");
        ContentValues values = new ContentValues();

        //向数据库中分别添加娱乐、财经、军事数据
        for (int i = 0; i < 10; i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            System.out.println("插入时" + str);
            switch (i) {
                case 0:
                    values.put("news_title", "“冲盈CP”活久见 李亚鹏许晴组饭局 ");
                    values.put("news_type", "娱乐");
                    values.put("publish_house", "网易");
                    values.put("news_content", "\t\t近日，《笑傲江湖》“冲盈CP”李亚鹏、" +
                            "许晴约饭、窦靖童作陪。许晴和李亚鹏自2001版《笑傲江湖》" +
                            "合作之后至今已17年，看来革命友谊真真是铁打的了！");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 1:
                    values.put("news_title", "全球最贵十大城市排行：中国无一城市上榜！ ");
                    values.put("news_type", "财经");
                    values.put("publish_house", "新浪");
                    values.put("news_content", "\t\t据瑞士《新苏黎世报》网站5月29日报道，" +
                            "近日瑞银集团公布了最新的“全球最贵城市”排名。第一名：苏黎世（瑞士）。" +
                            "瑞银集团最新公布的排名显示，苏黎世是全球最贵城市。" +
                            "不过，苏黎世的人均工资水平也属一流，在入围的77个城市中排名第二位，" +
                            "仅落后于日内瓦。“全球最贵城市”前十名中，中国没有一个城市上榜。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 2:
                    values.put("news_title", "海军演习反水雷无人潜航器登场");
                    values.put("news_type", "军事");
                    values.put("publish_house", "中华");
                    values.put("news_content", "\t\t5月下旬，东海舰队某基地组织多型数十艘水面舰艇、" +
                            "潜艇及数架次飞机，快速机动至某海域，进行了为期数天的多兵种“红蓝”" +
                            "实兵对抗水雷战演练，成功实布实扫实猎各型水雷10余枚，在近似实战条件下" +
                            "重点研练了渗透与反渗透、布雷与防雷观察、反水雷作战三大课题，部队战斗力得到进一步提升。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 3:
                    values.put("news_title", "最佳前任！班杰参加刘雨柔婚礼 到场先和新郎聊天 ");
                    values.put("news_type", "娱乐");
                    values.put("publish_house", "网易");
                    values.put("news_content", "\t\t据台湾媒体报道，刘雨柔和班杰曾交往过，但分手后依旧是好友，她2日和格斗选手黄育仁举办婚礼，他大方带现任泰国华侨女友Cindy到场，手拿红包入场，一开始还和新郎寒暄聊天，简直就是“最佳前度”典范。\n" +
                            "\t\t班杰身穿蓝色西装外套，在刘雨柔晚上7点婚宴前准时到场，他一开始和新郎聊天，之后拿出红包签名时，还被众人提醒：“你是女方那边”，他连忙走到女方收礼区，他全程和女友甜蜜蜜牵手进场，还跟媒体挥手，但低调不受访。\n" +
                            "\t\t刘雨柔筹备婚礼一年，特地出动了保镖到现场来维护一切的秩序，避免有任何差错，她说：“真的压力好大，因为我赔不起，所以我只能动作不要太大，像是树懒，有多慢走多慢。”婚宴主题是格斗擂台，十分特别。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 4:
                    values.put("news_title", "歼20弹舱敞开亮出新型导弹");
                    values.put("news_type", "军事");
                    values.put("publish_house", "中华");
                    values.put("news_content", "\t\t据央视报道，近日，解放军空军歼-20战机部队进行了一场实战演练，内容为歼-20战机与歼-16、歼-10C战机演练编队突击突防。在公开报道的画面中，歼-20机身两侧的弹舱开启，露出了一款新型空空导弹。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 5:
                    values.put("news_title", "新加坡将承担美朝首脑会晤主办费用：\"做小小贡献\" ");
                    values.put("news_type", "财经");
                    values.put("publish_house", "中新");
                    values.put("news_content", "\t\t中新网6月3日电 据新加坡《联合早报》3日报道，新加坡国防部长黄永宏2日就朝美首脑会晤表示，新加坡将扮演好东道主的角色，目前正紧锣密鼓筹备以确保安全。黄永宏还表示，新加坡将承担主办会晤的费用，“是为这次历史性峰会所做出的小小贡献”。黄永宏透露，内政团队和新加坡武装部队的保安队伍正紧锣密鼓地筹备，确保一切安全，“我也知道尤其是外交部的官员正非常卖力地工作，以我们各自的小小方式付出，让峰会可顺利进行”。\n" +
                            "\t\t被问及新加坡是否必须承担主办特金会的费用，黄永宏说：“这是必然的，但这是我们愿意负担的费用，是为这次历史性峰会所做出的小小贡献。”\n" +
                            "\t\t至于准备工作的进展，黄永宏表示保安工作已做足，其他方面的细节则得询问策划人，“这次峰会是具有建设性的，希望它能顺利举行，取得最好的成果”。\n" +
                            "\t\t美国总统特朗普当地时间6月1日在白宫会见朝鲜劳动党中央副委员长金英哲后，宣布美朝领导人将如期在本月12日于新加坡会面。\n" +
                            "\t\t目前，新加坡的多个地点因有望成为这次历史性会晤的举办场地，被外界密切关注。新加坡香格里拉大酒店、新加坡市中心的浮尔顿酒店、圣陶沙岛上的嘉佩乐酒店及新加坡总统府都被认为是可能举行会晤的热门场所。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 6:
                    values.put("news_title", "双胞胎被指“越大越不像” By2解释:妆容难免有异 ");
                    values.put("news_type", "娱乐");
                    values.put("publish_house", "网易");
                    values.put("news_content", "\t\t据台湾媒体报道，双胞胎女团“By2”今为自创品牌赴台湾办活动，妈妈一同出席与粉丝同欢；出道近10年的By2，常被指整型，网友认为两人刚出道时长得很像，近几年越来越不像，姐姐Miko解释：“每段时期造型不一样、妆容也会变，难免会有差。”妈妈得知负面批评后会不会心疼？姊妹俩表示：“不会跟她说，也不让她看这些新闻。”\n" +
                            "\t\t26岁的Miko与妹妹Yumi，透露很多朋友提醒她们女人25岁后新陈代谢变慢，要注重保养；问她们身体哪个部位对年龄增长最有感？两人笑说“全身的肉都垂下来”，尤其臀部最明显，“臀部不容易练，练起来才不会往下掉”；不担心胸部受地心引力影响？“胸部垫一垫就好了，马甲线比较难”！问两人对自己五官还有那里不满意？Miko词不达意回“想剃光头”，考虑剪短发突破风格。\n" +
                            "\t\t聊起感情生活，姊妹俩异口同声都说目前单身，Miko说：“虽然妈妈没催我们，但身边很多朋友都结婚了，看大家拼老公、我们拼事业，我们除了事业都没有真正的依靠，我跟妹妹会思考，会往这部分迈进。”不排斥与圈内人交往。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 7:
                    values.put("news_title", "俄侦察船穿越英吉利海峡 英国急派军舰战机监视 ");
                    values.put("news_type", "军事");
                    values.put("publish_house", "环球网");
                    values.put("news_content", "\t\t【环球网军事报道】据《每日星报》、《独立报》等媒体6月1日报道，一艘俄罗斯军舰近日穿过英吉利海峡时，英国方面随即出动一艘驱逐舰及一架直升机对其进行跟踪和监视。\n" +
                            "\t\t当地时间5月30日，英国海军派出“钻石号”驱逐舰和一架直升机前往追踪接近英国海岸的俄罗斯“扬塔尔号”侦察船。英媒指出，“扬塔尔号”是一所配备了两艘无人潜水器、可以下潜到海床并发回照片的专业侦察船。\n" +
                            "\t\t英国海军发言人表示：“5月30日傍晚，英国海军的钻石号驱逐舰冒着浓雾从朴茨茅斯出动，接替在比斯开湾负责监视的法国军舰。‘钻石号’随后发现了‘扬塔尔号’，并将继续监视其向北移动的活动。”“钻石号”指挥官吉斯则强调，舰上人员训练有素，可以在短时间内被调遣执行类似任务。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 8:
                    values.put("news_title", "歼20战机参加夜间对抗训练罕见照片曝光 ");
                    values.put("news_type", "军事");
                    values.put("publish_house", "新华社");
                    values.put("news_content", "\t\t新华社北京5月31日电（记者张玉清、张汨汨） 最早列装中国自主研制的新一代隐身战斗机歼－20的空军某部，日前开展歼－20与歼－16、歼－10C新型战机编队协同战术训练，不断探索提高歼－20作战性能，为空军新质作战能力跃升提供有力支撑。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
                case 9:
                    values.put("news_title", "默克尔表态拒绝为意大利减免债务 ");
                    values.put("news_type", "财经");
                    values.put("publish_house", "中新网");
                    values.put("news_content", "\t\t中新社柏林6月2日电 (记者 彭大伟)针对意大利新政府上台前夕引发争议的债务减免问题，德国总理默克尔2日明确表态拒绝这一选项。她表示，欧元区的内部团结任何时候都不可以“通向一个分摊债务的联盟”。\n" +
                            "\t\t5月31日，意大利技术政府候任总理科塔雷利向该国总统马塔雷拉提交退出组阁任务请求。朱塞佩·孔特被重新任命为总理，新政府已于日前宣誓就职。据德媒报道，意大利新政府由民粹主义的五星运动和极右色彩的北方联盟共同执政。在新政府上台前，关于执政党或将寻求令欧盟减免其2500亿欧元债务的消息便引发市场关注。\n" +
                            "\t\t本月2日，默克尔向德国《星期日法兰克福汇报》表示，她将不会揣测意大利新政府的意图，而是带着一种开放的态度与之接触并共事。\n" +
                            "\t\t针对意大利现执政党领袖此前关于意大利在欧元区去留问题的表态，以及意大利“不是德法奴隶”等言论，默克尔回应称：“如果我们能就事论事地展开对话，这样对所有人都更有利”。\n" +
                            "\t\t默克尔当天再度强调了坚持欧元区各国之间内部团结的态度。不过针对意大利国内希望减免债务的声音，她亦警告称，这种内部团结任何时候都不可以发展为“分摊债务的联盟”。\n" +
                            "\t\t默克尔表示，她已准备好同意大利新政府围绕改善青年人就业状况的议题展开对话。\n" +
                            "\t\t德国联邦政府副发言人菲茨2日表示，默克尔当天已与意大利新任总理朱塞佩·孔特通过电话。双方均强调继续推动德国和意大利紧密合作具有重要意义。默克尔邀请朱塞佩·孔特近期访德。德媒则预计，两人或将在数日后于加拿大举行的七国集团峰会期间举行首次会晤。");
                    values.put("comment_count", 0);
                    values.put("publish_date", str);
                    break;
            }
            Uri result = cr.insert(uri, values);
        }
        System.out.println("添加数据成功！");
//        Toast.makeText(MainActivity.this,
//                "添加数据成功！", Toast.LENGTH_SHORT).show();
    }

    //删除数据库中已有数据
    public void deleteData(){
        ContentResolver cr = MainActivity.this.getContentResolver();
        Uri uri = Uri.parse
                ("content://com.example.a123.lookhot.news/hotlook_table");
        cr.delete(uri,null,null);
        System.out.println("删除数据成功！");
    }
//实现轮播--初始化布局
    private void initViews() {
        vp = (ViewPager) findViewById(R.id.vp);
        vp.setOnPageChangeListener(this);
        ll= (LinearLayout) findViewById(R.id.point_container);
        //图片地址数组，将要实现轮播的图片地址放这数组里
        imas = new int[]{R.drawable.roll1, R.drawable.roll2, R.drawable.roll3, R.drawable.roll4 };
        ImageView iv;
        //图片数组
        imaList = new ArrayList<ImageView>();
        View pointview;
//        根据图片地址数组将对应图片放入imaList中
        for (int i = 0; i < imas.length; i++) {
            iv = new ImageView(this);
//            图片地址放入对应图片
            iv.setImageResource(imas[i]);
//            设置图片按X轴、Y轴方向拉伸
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            imaList.add(iv);

            //加小白点
            pointview =new View(this);
            pointview.setEnabled(false);
            pointview.setBackgroundResource(R.drawable.selector);
//            设置小白点的布局LinearLayout的宽高（小白点的宽高）
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(15,15);
            if(i!=0)
                params.leftMargin=10;
            ll.addView(pointview,
                    params );
        }

    }

    //    实现轮播--适配器
    private void initAdapter() {
        ll.getChildAt(0).setEnabled(true);
        vp.setAdapter(new MyAdapter());
        vp.setCurrentItem(Integer.MAX_VALUE/2+1);//这里以一个除以4为0的数开始，这样打开默认会在第0个item显示
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag=false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<ll.getChildCount();i++){
            ll.getChildAt(i).setEnabled(false);
        }
        ll.getChildAt(position%4).setEnabled(true);//设置小白点
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newposition = position % 4;//得到新位置
            ImageView imageView = imaList.get(newposition);
            container.addView(imageView);
            return imageView;//把View对象返回给框架，必须重写
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override //判断复用
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    //enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
    private void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
//        setIconEnable(menu, true);            //经测试不用也可以显示图标
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.news_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public   boolean onOptionsItemSelected(MenuItem item){
        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        switch (item.getItemId()){
            case R.id.add_news:
                addNewsData();
                startActivity(intent);
                return true;
            case R.id.delete_news:
                deleteData();
                startActivity(intent);
                return true;
            case R.id.refresh_news:
                startActivity(intent);
                return true;
            default:
                return  super.onOptionsItemSelected(item);

        }
    }
}
