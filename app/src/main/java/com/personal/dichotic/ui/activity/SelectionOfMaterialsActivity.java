package com.personal.dichotic.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.personal.dichotic.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rocky on 2018/4/16.
 */

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_item0)
    TextView tvItem0;
    @BindView(R.id.tv_item1)
    TextView tvItem1;
    @BindView(R.id.tv_item2)
    TextView tvItem2;
    @BindView(R.id.tb_find)
    Toolbar tbFind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);
        tbFind.setNavigationIcon(R.mipmap.ic_arrow_left);
        setSupportActionBar(tbFind);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initEvent();
    }

    private void initEvent() {
        tvItem0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("isTestTone",true);
                intent.putExtra("DesContent","您将会听到一个单音节词，请选择出您听到的词，并熟悉这些词，以进行正式测试。");
                showdialog(intent);

            }
        });
        tvItem2.setOnClickListener(this);
        tvItem1.setOnClickListener(this);
        tbFind.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_item1:
                intent.putExtra("isFirstTone", true);
                break;
            case R.id.tv_item2:
                intent.putExtra("isFirstTone", false);
                break;
            default:
                break;
        }
        intent.setClass(ThirdActivity.this, FourthActivity.class);
        startActivity(intent);
    }
    private void showdialog(Intent intent) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ThirdActivity.this);
        builder.setMessage("请将耳机戴入相对应的耳别,并将音量调到最舒适强度");
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                intent.setClass(ThirdActivity.this, FifthActivity.class);
                intent.putExtra("title","测试练习");
                startActivity(intent);

            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
