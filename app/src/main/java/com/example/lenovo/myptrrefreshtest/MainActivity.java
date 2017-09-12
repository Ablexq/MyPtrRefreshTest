package com.example.lenovo.myptrrefreshtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvDrag)
    TextView tvDrag;
    @BindView(R.id.ptrFrame_custom)
    PtrFrameLayout ptrFrame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulltorefresh_custom);
        ButterKnife.bind(this);

        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //请求数据
                        Log.e("111111","请求数据");
                        //刷新结束
                        ptrFrame.refreshComplete();
                    }
                }, 2000);
            }
        });
    }

}
