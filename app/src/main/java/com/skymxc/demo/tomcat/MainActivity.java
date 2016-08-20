package com.skymxc.demo.tomcat;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private SurfaceView sv  ;

    //SurfaceView的句柄 控制 SurfaceView ； 持有，查找 等
    private SurfaceHolder holder ;

    //矩形
    private  Rect dst ;

    //资源
    private Resources resources;

    //第一项 cymbal
    private Object[] cymbal ={"cymbal_",13};

    //第二项 scratch
    private Object[] scratch = {"scratch_" ,56};

    //第三项 pie
    private Object[] pie = {"pie_",24};

    //第四项 fart
    private Object[] fart ={"fart_",28};

    //第五项 drink
    private Object[] drink ={"drink_",81};

    //第六项 eat
    private Object[] eat ={"eat_",40};

    //踩脚
    private Object[] knockout= {"knockout_",81};

    //临时图片
    Object[] temp = null;

    //声音池 短声音播放
    private SoundPool soundPool ;

    //是否正在播放动画
    private boolean isPlaying =false ;

    //声音池id集合
    private List<Integer>  soundIds = null;

    //声音资源文件 顺序要和图片 顺序一致
    private int[] resids ={R.raw.cymbal,R.raw.scratch,R.raw.pie,R.raw.fart,R.raw.drink,R.raw.eat,R.raw.knockout};

    //定时器
    private Timer timer =null;

    //音频池的id
    private int index ;

    //每个项的声音延迟  毫秒  按每项的顺序
    private  int[] delay = {0,2000,1000,200,5000,2000,0};

    /**
     * 创建activity时调用的方法
     * 进行初始化的操作
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载布局文件 设置显示的内容
        setContentView(R.layout.activity_main);

        //通过id查找视图 只会在上面设置的内容中查找  surfaceView 特点必须自己先创建完 才能执行 ，可以通过句柄得知是否初始化完成
        sv= (SurfaceView) findViewById(R.id.surface);

        //得到 surface的句柄
        holder=  sv.getHolder();

        //添加回调
        holder.addCallback(callback);

        //参数1 最多几个声音 参数2 声音类型 AudioManager是声音管理 各种静态类型
        soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC,1);
        //初始化 资源id集合
        soundIds = new ArrayList<>();
        //遍历音频资源数组 加载到声音池
        Log.e("tag","======init========="+soundPool);
        for(int a: resids){

            //加载一个声音，加载到声音池 参数1 上下文，参数2. 音频资源id  参数3 播放的优先级\
            //返回一个 id 音频在声音池的id
            int id = soundPool.load(MainActivity.this,a,1);

            //将声音池的id添加到 集合
            soundIds.add(id);
        }

        // 初始化 Timer
        timer= new Timer();




    }

    /**
     * 回调
     * 作用：
     *  告诉 holder surface是否创建完了
     */
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

        /**
         * 通知 surface 已经创建完了
         * @param holder
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i("tag","======begin set surfaceView =========");


            //获取surfaceView 的宽高
            int svWidth= sv.getWidth();
            int svHeight = sv.getHeight();
            Log.i("tag",svWidth+"======surfaceCreated========="+svHeight);
            //目标矩形 画布上的矩形
            dst = new Rect(0,0,svWidth,svHeight);

            //拿到图片     getRescources 是获取应用资源 包括res 下所有的资源
            resources= getResources();
            drawBitmap(R.mipmap.cymbal_12);

            Log.e("tag","============over=============");

        }

        /**
         * 通知 surface 发生了改变  尺寸发生变化时
         * @param holder
         * @param format
         * @param width 宽度
         * @param height 高度
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        /**
         * 通知 surface已经销毁了
         * @param holder
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };


    /**
     * 根据id获取 位图
     * @param id
     * @return
     */
    private Bitmap getBitmap(int id ){
        //获取到资源中的可绘制资源(图片) 参数 资源id
        Drawable drawable= resources.getDrawable(id);

        //子类型； Drawable 有多种类型的 例如 颜色绘制的图
        BitmapDrawable bmpDaw = (BitmapDrawable) drawable;

        //获取可绘制资源中的位图
        Bitmap bmp = bmpDaw.getBitmap();
        return bmp;
    }
    /**
     * 绘制图`片
     *
     */
    private void drawBitmap(int id ) {
        //锁定并拿到画布
        Canvas canvas = holder.lockCanvas();


        Bitmap bmp = getBitmap(id);

        //获取位图的像素宽高
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        //创建矩形 要绘制的位图
        Rect src = new Rect(0,0,bmpWidth,bmpHeight);

        /**
         * 画一个位图 图片对应到程序中就是位图
         * 参数
         *  bmp 图片位图
         *  src，要绘制的矩形 位图矩形
         *  dst  目标矩形 画布中的
         *  null 画笔  现在不需要
         */
        canvas.drawBitmap(bmp,src,dst,null);

        //绘制完毕 接触画布的锁定 并且 寄送画布
        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * 点击事件
     *      规定：
     *          修饰符 必须 public
     *          返回值 必须 void
     *          方法名称 大小写区分 xml文件必须一致
     *          参数 必须是一个View 只能有一个view
     * @param v 被点击的view
     */
    public void click(View v){
        if (isPlaying){
            Toast.makeText(MainActivity.this,"再快点屏就戳烂了，慢点戳！",Toast.LENGTH_SHORT).show();
            return;
        }
        //获取被点击view的id
       int id = v.getId();


        //匹配id
        switch (id){
            case  R.id.cymbal:
                Log.e("aaa","==================cymbal==============");
                temp = cymbal;
                index=0;
                break;
            case  R.id.drink:
                Log.e("aaa","==================drink==============");
                temp = drink;
                index=4;
                break;
            case  R.id.eat:
                Log.e("aaa","==================eat==============");
                temp=eat;
                index=5;
                break;
            case  R.id.fart:
                Log.e("aaa","==================fart==============");
                temp = fart;
                index=3;
                break;
            case  R.id.pie:
                Log.e("aaa","==================pie==============");
                temp = pie ;
                index=2;
                break;
            case  R.id.scratch:
                Log.e("aaa","==================scratch==============");
                temp = scratch;
                index=1;
                break;
            default:
                return;
        }
        startAnimation(temp);

    }


    private void startAnimation(final Object[] temp) {
        //一个新线程
        new Thread(){
            @Override
            public void run() {
                isPlaying=true;

             //利用timer 进行延时  ，TimerTask的一个对象只能执行一回
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //播放声音
                    int id =  soundIds.get(index);
                    //播放声音 ，
                    // 参数1 是 声音池的id，参数2左声道，参数3右声道，参数5是循环参数
                    //参数 4 播放优先级，参数6比特率
                    soundPool.play(id,1,1,1,0,1);
                }
            }, delay[index]);


                //获取应用包名
                String pgkName =getPackageName();
                for (int i=0;i<(Integer)temp[1];i++){

                    //资源名
                    String name =i<10?temp[0].toString()+0+i:temp[0].toString()+i;

                    //获取资源id 参数1 资源名(无后缀)，参数2 哪个文件夹，参数3 包名
                    int rId = resources.getIdentifier(name,"mipmap",pgkName);
                    drawBitmap(rId);

                    //睡眠60ms
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                isPlaying=false;

            }
        }.start();
    }


    //统计触发几次
    int count  ;

    /**
     * 屏幕触摸事件
     * @param event 一次触摸事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 触摸:按下 移动 ，抬起

       if (isPlaying) {
        Toast.makeText(MainActivity.this,"wait a little",Toast.LENGTH_SHORT);

       }else {
           //event.getAction() 获取事件的类型
           if (event.getAction() == MotionEvent.ACTION_DOWN) {
               //按下
               count++;
               if (count == 5) {
                   count = 0;
                   index = 6;  //改变下标
                   startAnimation(knockout);
               }
           }
       }
        return super.onTouchEvent(event);
    }

}
