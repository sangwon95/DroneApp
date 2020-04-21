package com.example.firebaesactivity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.firebaesactivity.R;
import com.google.firebase.database.annotations.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getSystemService;

public class FinedustFragment extends Fragment implements TextView.OnEditorActionListener {


    private EditText editText;
    private ListView listview;
   // private LinearLayout linefine;
    private Button button;
    private ArrayList<String> city = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();

    private String key="";
    private int city_number =0;
    private String dataTime,cityName,pm10Value,pm25Value;
    private View view;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_finedust, container, false);

        editText = view.findViewById(R.id.search_edt);
        listview = view.findViewById(R.id.listview);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setOnEditorActionListener(this);

        return view;
    }

    public ArrayList<String> getXmlData(){

        String search_city_name = editText.getText().toString();

        switch (search_city_name)
        {
            case "대전":
            case "광주":
            case "울산": city_number=5;
                break;
            case "서울": city_number=25;
                break;
            case "부산": city_number=16;
                break;
            case "대구": city_number=8;
                break;
            case "충북": city_number=11;
                break;
            case "충남": city_number=15;
                break;
            case "인천": city_number=10;
                break;
            case "경기": city_number=31;
                break;
            case "경남": city_number=18;
            case "강원": city_number=18;
                break;
            case "전북": city_number=14;
                break;
            case "전남": city_number=22;
                break;
            case "경북": city_number=23;
                break;
            case "제주": city_number=2;
                break;
            case "세종": city_number=1;
                break;
        }
        String queryUrl="http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?serviceKey="+key+"&numOfRows="+city_number+"&pageNo=1&sidoName="+search_city_name+"&searchCondition=DAILY";

        try {

            URL url= new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성
            InputStream is= url.openStream(); // url위치로 인풋스트림 연결
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            // inputstream 으로부터 xml 입력받기
            parser .setInput( new InputStreamReader(is, "UTF-8") );
            String tag;
            parser .next();

            int eventType= parser.getEventType();

            city.clear();
            while( eventType != XmlPullParser.END_DOCUMENT ){

                switch( eventType ){

                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= parser.getName(); // 태그 이름 얻어오기

                        if(tag.equals("item")) ; // 첫번째 검색결과
                        else if(tag.equals("dataTime")){
                            parser.next();
                            dataTime = parser.getText();

                        }

                        else if(tag.equals("cityName")){
                            parser .next();

                            cityName = parser .getText();
                        }
                        else if(tag.equals("pm10Value")){
                            parser .next();
                            pm10Value = parser .getText();

                        }
                        else if(tag.equals("pm25Value")){
                            parser .next();
                            pm25Value=parser.getText();
                            city.add("\n"+"  지역: "+cityName+"\n"+"  미세먼지: "+pm10Value+"㎍/㎥"+"   초미세먼지: "+pm25Value+"㎍/㎥"+"\n"+"  측정시간: "+dataTime+"\n");

                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        /*tag= parser .getName(); // 태그 이름 얻어오기
                        if(tag.equals("pm25Value")) buffer.append("\n"); // 첫번째 검색결과 끝 줄바꿈*/
                        break;

                }
                eventType= parser.next(); // 종료 태그 파싱
            }


        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();

        }

        return city;
    }


    // Button을 클릭했을 때 쓰레드 통해 해당 함수 실행
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(v.getId()==R.id.search_edt&& actionId==EditorInfo.IME_ACTION_SEARCH){

            InputMethodManager mInputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    data = getXmlData();
                    // 아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,data);
                            adapter.notifyDataSetChanged();
                            listview.setAdapter(adapter);
                        }
                    });
                }
            }).start();

        }
        return true;
    }
}