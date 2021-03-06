package org.androidtown.hotplace;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3;
    String choice_do = "";
    String choice_se = "";

    ImageView WeatherIconImg;
    TextView now_temp;
    TextView max_temp;
    TextView min_temp;
    TextView temp_div;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SearchTrafficMapFragment searchTrafficMapFragment;

    Bundle bundle;
    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //액션바
        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바 어플리케이션 이름 삭제
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        now_temp = (TextView) findViewById(R.id.search_weather_nowtemp);
        max_temp = (TextView) findViewById(R.id.search_weather_maxtemp);
        min_temp = (TextView) findViewById(R.id.search_weather_mintemp);
        WeatherIconImg = (ImageView) findViewById(R.id.search_weather_icon);
        temp_div = (TextView) findViewById(R.id.search_maxmintemp_div);
        temp_div.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.spinner);
        spinner.setSelection(0);
        spinner2 = findViewById(R.id.spinner2);
        spinner2.setSelection(0);
        spinner3 = findViewById(R.id.spinner3);
        spinner3.setSelection(0);

        adspin1 = ArrayAdapter.createFromResource(this, R.array.광역자치단체, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adspin1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchTrafficMapFragment = new SearchTrafficMapFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                bundle = new Bundle();

                if (adspin1.getItem(position).equals("서울특별시")) {
                    choice_do = "서울특별시";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.서울특별시, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("강서구")) {
                                choice_do = "강서구";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.강서구, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Seoul");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.560067);
                                bundle.putDouble("search_Longitude", 126.822551);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("부산광역시")) {
                    choice_do = "부산광역시";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.부산광역시, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("기장군")) {
                                choice_do = "기장군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.기장군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kijang");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.263681);
                                bundle.putDouble("search_Longitude", 129.224700);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("광주광역시")) {
                    choice_do = "광주광역시";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.광주광역시, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("광산구")) {
                                choice_do = "광산구";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.광산구, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Gwangju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.135154);
                                bundle.putDouble("search_Longitude", 126.788463);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("대구광역시")) {
                    choice_do = "대구광역시";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.대구광역시, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("달서구")) {
                                choice_do = "달서구";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.달서구, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Daegu");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.829183);
                                bundle.putDouble("search_Longitude", 128.529102);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("대전광역시")) {
                    choice_do = "대전광역시";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.대전광역시, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("서구")) {
                                choice_do = "서구";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.서구, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Daejeon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.340588);
                                bundle.putDouble("search_Longitude", 127.380601);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("울산광역시")) {
                    choice_do = "울산광역시";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.울산광역시, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("울주군")) {
                                choice_do = "울주군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.울주군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Ulsan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.431162);
                                bundle.putDouble("search_Longitude", 129.340414);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("인천광역시")) {
                    choice_do = "인천광역시";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.인천광역시, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("강화군")) {
                                choice_do = "강화군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.강화군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kanghwa");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.709357);
                                bundle.putDouble("search_Longitude", 126.452580);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("제주특별자치도")) {
                    choice_do = "제주특별자치도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.제주특별자치도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("제주시")) {
                                choice_do = "제주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.제주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("애월읍")) {
                                            find_weather("Gaigeturi");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 33.455305);
                                            bundle.putDouble("search_Longitude", 126.326646);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("경기도")) {
                    choice_do = "경기도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.경기도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("파주시")) {
                                choice_do = "파주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.파주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("문산읍")) {
                                            find_weather("Munsan");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 37.861703);
                                            bundle.putDouble("search_Longitude", 126.788306);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("포천시")) {
                                choice_do = "포천시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.포천시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("창수면")) {
                                            find_weather("Changsu");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 37.991188);
                                            bundle.putDouble("search_Longitude", 127.188818);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("이천시")) {
                                choice_do = "이천시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.이천시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("부발읍")) {
                                            find_weather("Pubal");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 37.283160);
                                            bundle.putDouble("search_Longitude", 127.488413);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("남양주시")) {
                                choice_do = "남양주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.남양주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("화도읍")) {
                                            find_weather("Hwado");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 37.651298);
                                            bundle.putDouble("search_Longitude", 127.306880);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                        else if(adspin3.getItem(position).equals("와부읍")) {
                                            find_weather("Wabu");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 37.587337);
                                            bundle.putDouble("search_Longitude", 127.217767);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("광주시")) {
                                choice_do = "광주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.광주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("이선리")) {
                                            find_weather("Eisen");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 37.357712);
                                            bundle.putDouble("search_Longitude", 127.424199);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("연천군")) {
                                choice_do = "연천군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.연천군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("yeoncheongun");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 38.099412);
                                bundle.putDouble("search_Longitude", 127.068874);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("여주군")) {
                                choice_do = "여주군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.여주군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Yeoju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.296487);
                                bundle.putDouble("search_Longitude", 127.617060);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("양평군")) {
                                choice_do = "양평군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.양평군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Yangpyong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.490443);
                                bundle.putDouble("search_Longitude", 127.494238);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("양주시")) {
                                choice_do = "양주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.양주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Yangju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.804847);
                                bundle.putDouble("search_Longitude", 126.997120);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("의정부시")) {
                                choice_do = "의정부시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.의정부시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Vijongbu");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.736938);
                                bundle.putDouble("search_Longitude", 127.058781);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("수원시")) {
                                choice_do = "수원시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.수원시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Suigen");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.280661);
                                bundle.putDouble("search_Longitude", 127.007488);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("부천시")) {
                                choice_do = "부천시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.부천시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Bucheon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.503980);
                                bundle.putDouble("search_Longitude", 126.785688);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("오산시")) {
                                choice_do = "오산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.오산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Osan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.161845);
                                bundle.putDouble("search_Longitude", 127.051822);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("구리시")) {
                                choice_do = "구리시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.구리시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kuri");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.597230);
                                bundle.putDouble("search_Longitude", 127.130928);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("고양시")) {
                                choice_do = "고양시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.고양시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Goyang");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.660936);
                                bundle.putDouble("search_Longitude", 126.832317);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("가평군")) {
                                choice_do = "가평군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.가평군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Gapyeong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.825333);
                                bundle.putDouble("search_Longitude", 127.510952);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("화성시")) {
                                choice_do = "화성시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.화성시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Hwaseong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.209160);
                                bundle.putDouble("search_Longitude", 127.052128);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("안양시")) {
                                choice_do = "안양시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.안양시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Anyang");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.401908);
                                bundle.putDouble("search_Longitude", 126.922459);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("안성시")) {
                                choice_do = "안성시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.안성시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Anseong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.011814);
                                bundle.putDouble("search_Longitude", 127.265906);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("안산시")) {
                                choice_do = "안산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.안산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Ansan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.321227);
                                bundle.putDouble("search_Longitude", 126.829690);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("성남시")) {
                                choice_do = "성남시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.성남시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Seongnam");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.404857);
                                bundle.putDouble("search_Longitude", 127.116232);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("하남시")) {
                                choice_do = "하남시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.하남시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Hanam");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.541103);
                                bundle.putDouble("search_Longitude", 127.205237);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("광명시")) {
                                choice_do = "광명시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.광명시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kwangmyong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.475438);
                                bundle.putDouble("search_Longitude", 126.866094);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("강원도")) {
                    choice_do = "강원도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.강원도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("영월군")) {
                                choice_do = "영월군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.영월군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Neietsu");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.186723);
                                bundle.putDouble("search_Longitude", 128.465713);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("양구군")) {
                                choice_do = "양구군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.양구군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Yanggu");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 38.106001);
                                bundle.putDouble("search_Longitude", 127.989075);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("원주시")) {
                                choice_do = "원주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.원주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Wonju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.344178);
                                bundle.putDouble("search_Longitude", 127.951441);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("태백시")) {
                                choice_do = "태백시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.태백시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Taebaek");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.162163);
                                bundle.putDouble("search_Longitude", 128.987005);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("속초시")) {
                                choice_do = "속초시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.속초시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Sogcho");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 38.206993);
                                bundle.putDouble("search_Longitude", 128.577170);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("삼척시")) {
                                choice_do = "삼척시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.삼척시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Santyoku");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.443957);
                                bundle.putDouble("search_Longitude", 129.159443);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("강릉시")) {
                                choice_do = "강릉시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.강릉시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kang-neung");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.757995);
                                bundle.putDouble("search_Longitude", 128.891419);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("화천군")) {
                                choice_do = "화천군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.화천군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Hwacheon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 38.105383);
                                bundle.putDouble("search_Longitude", 127.706252);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("홍천군")) {
                                choice_do = "홍천군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.홍천군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Hongchon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.690009);
                                bundle.putDouble("search_Longitude", 127.884168);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("춘천시")) {
                                choice_do = "춘천시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.춘천시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Chuncheon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.887489);
                                bundle.putDouble("search_Longitude", 127.738713);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("동해시")) {
                                choice_do = "동해시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.동해시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Tonghae");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 37.522638);
                                bundle.putDouble("search_Longitude", 129.108875);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("충청남도")) {
                    choice_do = "충청남도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.충청남도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("공주시")) {
                                choice_do = "공주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.공주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("태산리")) {
                                            find_weather("Taesal-li");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 36.554720);
                                            bundle.putDouble("search_Longitude", 127.192761);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("금산군")) {
                                choice_do = "금산군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.금산군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("전산면")) {
                                            find_weather("Jenzan");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 36.143337);
                                            bundle.putDouble("search_Longitude", 127.370681);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("서천군")) {
                                choice_do = "서천군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.서천군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("기산면")) {
                                            find_weather("Keizan");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 36.074144);
                                            bundle.putDouble("search_Longitude", 126.761912);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("아산시")) {
                                choice_do = "아산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.아산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("배방읍")) {
                                            find_weather("Tenan");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 36.774336);
                                            bundle.putDouble("search_Longitude", 127.056738);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("논산시")) {
                                choice_do = "논산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.논산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("연무읍")) {
                                            find_weather("Yonmu");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 36.125704);
                                            bundle.putDouble("search_Longitude", 127.098282);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("예산군")) {
                                choice_do = "예산군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.예산군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Yesan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.683169);
                                bundle.putDouble("search_Longitude", 126.839395);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("당진시")) {
                                choice_do = "당진시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.당진시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Tangjin");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.905013);
                                bundle.putDouble("search_Longitude", 126.648212);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("보령시")) {
                                choice_do = "보령시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.보령시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Taisen-ri");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.348698);
                                bundle.putDouble("search_Longitude", 126.598508);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("서산시")) {
                                choice_do = "서산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.서산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Suisan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.783060);
                                bundle.putDouble("search_Longitude", 126.450980);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("부여군")) {
                                choice_do = "부여군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.부여군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Fuyo");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.275822);
                                bundle.putDouble("search_Longitude", 126.910506);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("홍성군")) {
                                choice_do = "홍성군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.홍성군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Hongsung");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.600776);
                                bundle.putDouble("search_Longitude", 126.661922);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("충청북도")) {
                    choice_do = "충청북도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.충청북도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("영동군")) {
                                choice_do = "영동군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.영동군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Yong-dong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.174431);
                                bundle.putDouble("search_Longitude", 127.779149);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("옥천군")) {
                                choice_do = "옥천군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.옥천군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("ogcheongun");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.306148);
                                bundle.putDouble("search_Longitude", 127.574824);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("청주시")) {
                                choice_do = "청주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.청주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Cheongju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.628943);
                                bundle.putDouble("search_Longitude", 127.480681);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("괴산군")) {
                                choice_do = "괴산군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.괴산군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Koesan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.810374);
                                bundle.putDouble("search_Longitude", 127.790759);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("진천군")) {
                                choice_do = "진천군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.진천군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Chinchon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.858973);
                                bundle.putDouble("search_Longitude", 127.443160);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("전라남도")) {
                    choice_do = "전라남도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.전라남도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("보성군")) {
                                choice_do = "보성군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.보성군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("벌교읍")) {
                                            find_weather("Beolgyo");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 34.846574);
                                            bundle.putDouble("search_Longitude", 127.341352);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("해남군")) {
                                choice_do = "해남군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.해남군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Haenam");
                                temp_div.setVisibility(View.VISIBLE);
                                bundle.putDouble("search_Latitude", 34.571918);
                                bundle.putDouble("search_Longitude", 126.596354);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("화원면")) {
                                            find_weather("Hwawon");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 34.670997);
                                            bundle.putDouble("search_Longitude", 126.327988);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("곡성군")) {
                                choice_do = "곡성군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.곡성군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("호곡리")) {
                                            find_weather("Hoko");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 35.247585);
                                            bundle.putDouble("search_Longitude", 127.366503);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("여수시")) {
                                choice_do = "여수시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.여수시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Reisui");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 34.763495);
                                bundle.putDouble("search_Longitude", 127.660951);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("영광군")) {
                                choice_do = "영광군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.영광군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Reiko");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.275622);
                                bundle.putDouble("search_Longitude", 126.508938);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("순천시")) {
                                choice_do = "순천시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.순천시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Sunchun");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 34.952792);
                                bundle.putDouble("search_Longitude", 127.512369);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("나주시")) {
                                choice_do = "나주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.나주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Naju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 34.996461);
                                bundle.putDouble("search_Longitude", 126.722486);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("목포시")) {
                                choice_do = "목포시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.목포시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Moppo");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 34.810861);
                                bundle.putDouble("search_Longitude", 126.391310);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("광양시")) {
                                choice_do = "광양시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.광양시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kwangyang");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 34.944218);
                                bundle.putDouble("search_Longitude", 127.704147);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("구례군")) {
                                choice_do = "구례군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.구례군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kurye");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.215362);
                                bundle.putDouble("search_Longitude", 127.473759);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("화순군")) {
                                choice_do = "화순군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.화순군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Hwasun");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.020263);
                                bundle.putDouble("search_Longitude", 127.025586);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("신안군")) {
                                choice_do = "신안군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.신안군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Sinan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 34.824799);
                                bundle.putDouble("search_Longitude", 126.100372);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("전라북도")) {
                    choice_do = "전라북도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.전라북도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("완주군")) {
                                choice_do = "완주군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.완주군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Wanju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.957998);
                                bundle.putDouble("search_Longitude", 127.133555);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("부안군")) {
                                choice_do = "부안군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.부안군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Puan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.726076);
                                bundle.putDouble("search_Longitude", 126.731682);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("남원시")) {
                                choice_do = "남원시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.남원시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Nangen");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.416036);
                                bundle.putDouble("search_Longitude", 127.388839);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("군산시")) {
                                choice_do = "군산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.군산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kunsan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.965833);
                                bundle.putDouble("search_Longitude", 126.730106);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("고창군")) {
                                choice_do = "고창군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.고창군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kochang");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.432052);
                                bundle.putDouble("search_Longitude", 126.703029);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("김제시")) {
                                choice_do = "김제시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.김제시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kimje");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.800520);
                                bundle.putDouble("search_Longitude", 126.888046);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("익산시")) {
                                choice_do = "익산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.익산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Iksan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.951149);
                                bundle.putDouble("search_Longitude", 126.972580);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("임실군")) {
                                choice_do = "임실군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.임실군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Imsil");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.577249);
                                bundle.putDouble("search_Longitude", 127.213319);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("전주시")) {
                                choice_do = "전주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.전주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Jeonju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.827024);
                                bundle.putDouble("search_Longitude", 127.115292);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("진안군")) {
                                choice_do = "진안군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.진안군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("jin-angun");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.788620);
                                bundle.putDouble("search_Longitude", 127.425897);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("경상남도")) {
                    choice_do = "경상남도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.경상남도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("거제시")) {
                                choice_do = "거제시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.거제시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kyosai");
                                temp_div.setVisibility(View.VISIBLE);
                                bundle.putDouble("search_Latitude", 34.893441);
                                bundle.putDouble("search_Longitude", 128.685118);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("신현읍")) {
                                            find_weather("Sinhyeon");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 34.880638);
                                            bundle.putDouble("search_Longitude", 128.622926);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("밀양시")) {
                                choice_do = "밀양시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.밀양시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Miryang");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.484311);
                                bundle.putDouble("search_Longitude", 128.752201);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("양산시")) {
                                choice_do = "양산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.양산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Yangsan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.338691);
                                bundle.putDouble("search_Longitude", 129.025319);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("웅상읍")) {
                                            find_weather("Ungsang");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 35.377010);
                                            bundle.putDouble("search_Longitude", 129.147798);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("창원시")) {
                                choice_do = "창원시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.창원시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Changwon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.254850);
                                bundle.putDouble("search_Longitude", 128.645197);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if(adspin3.getItem(position).equals("내서읍")) {
                                            find_weather("Naeso");
                                            temp_div.setVisibility(View.VISIBLE);

                                            bundle.putDouble("search_Latitude", 35.246330);
                                            bundle.putDouble("search_Longitude", 128.507312);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("마산시")) {
                                choice_do = "마산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.마산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Masan");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.214707);
                                bundle.putDouble("search_Longitude", 128.581347);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("고성군")) {
                                choice_do = "고성군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.고성군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Goseong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 34.976053);
                                bundle.putDouble("search_Longitude", 128.322462);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("김해시")) {
                                choice_do = "김해시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.김해시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kimhae");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.228598);
                                bundle.putDouble("search_Longitude", 128.865968);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("진주시")) {
                                choice_do = "진주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.진주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Chinju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.196821);
                                bundle.putDouble("search_Longitude", 128.117845);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("진해시")) {
                                choice_do = "진해시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.진해시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Chinhae");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.132807);
                                bundle.putDouble("search_Longitude", 128.706819);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("창녕군")) {
                                choice_do = "창녕군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.창녕군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Changnyeong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.534952);
                                bundle.putDouble("search_Longitude", 128.500861);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else if (adspin1.getItem(position).equals("경상북도")) {
                    choice_do = "경상북도";
                    adspin2 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.경상북도, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adspin2);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            if (adspin2.getItem(position).equals("포항시")) {
                                choice_do = "포항시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.포항시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if (adspin3.getItem(position).equals("흥해읍")) {
                                            find_weather("Heung-hai");
                                            temp_div.setVisibility(View.VISIBLE);
                                            bundle.putDouble("search_Latitude", 36.107004);
                                            bundle.putDouble("search_Longitude", 129.342592);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        } else if (adspin3.getItem(position).equals("연일읍")) {
                                            find_weather("Enjitsu");
                                            temp_div.setVisibility(View.VISIBLE);
                                            bundle.putDouble("search_Latitude", 35.992192);
                                            bundle.putDouble("search_Longitude", 129.343832);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("칠곡군")) {
                                choice_do = "칠곡군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.칠곡군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if (adspin3.getItem(position).equals("왜관읍")) {
                                            find_weather("Waegwan");
                                            temp_div.setVisibility(View.VISIBLE);
                                            bundle.putDouble("search_Latitude", 35.988651);
                                            bundle.putDouble("search_Longitude", 128.398968);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("경산시")) {
                                choice_do = "경산시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.경산시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                        if (adspin3.getItem(position).equals("하양읍")) {
                                            find_weather("Hayang");
                                            temp_div.setVisibility(View.VISIBLE);
                                            bundle.putDouble("search_Latitude", 35.912583);
                                            bundle.putDouble("search_Longitude", 128.818827);
                                            searchTrafficMapFragment.setArguments(bundle);
                                            fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("상주시")) {
                                choice_do = "상주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.상주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Sangju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.413513);
                                bundle.putDouble("search_Longitude", 128.159047);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("문경시")) {
                                choice_do = "문경시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.문경시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Mungyeong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.592048);
                                bundle.putDouble("search_Longitude", 128.193884);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("경주시")) {
                                choice_do = "경주시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.경주시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kyonju");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 35.839059);
                                bundle.putDouble("search_Longitude", 129.201941);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("군위군")) {
                                choice_do = "군위군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.군위군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kunwi");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.239654);
                                bundle.putDouble("search_Longitude", 128.570714);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("구미시")) {
                                choice_do = "구미시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.구미시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Kumi");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.115211);
                                bundle.putDouble("search_Longitude", 128.343100);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("김천시")) {
                                choice_do = "김천시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.김천시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Gimcheon");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.126649);
                                bundle.putDouble("search_Longitude", 128.099609);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("청송군")) {
                                choice_do = "청송군";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.청송군, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Cheongsong gun");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.352967);
                                bundle.putDouble("search_Longitude", 129.059221);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            } else if (adspin2.getItem(position).equals("안동시")) {
                                choice_do = "안동시";
                                adspin3 = ArrayAdapter.createFromResource(SearchActivity.this, R.array.안동시, android.R.layout.simple_spinner_dropdown_item);
                                adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner3.setAdapter(adspin3);

                                find_weather("Andong");
                                temp_div.setVisibility(View.VISIBLE);

                                bundle.putDouble("search_Latitude", 36.564766);
                                bundle.putDouble("search_Longitude", 128.720191);
                                searchTrafficMapFragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.traffic_map, searchTrafficMapFragment).commitAllowingStateLoss();

                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        choice_se = adspin3.getItem(position).toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //툴바 메뉴 선택 시 실행
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //뒤로가기 버튼 클릭시
                searchTrafficMapFragment.onDestroy();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //위도와 경도에 맞는 날씨 찾기
    public void find_weather(String cityname) {
        String current_url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityname + ",kr&appid=e6b1f229824c3f4eac0cf8000177ecc2";

        JsonObjectRequest jor_current = new JsonObjectRequest(Request.Method.GET, current_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //current weater에서는 오늘 하루 현재의 온도와 습도, 바람 방향, 세기, 아이콘 등을 가져온다.
                try {
                    JSONObject main_object = response.getJSONObject("main"); //객체인 main 가져오기 (temp, pressure, humidity, temp_min, temp_max)
                    JSONArray array = response.getJSONArray("weather"); //배열인 weather 가져오기
                    JSONObject object = array.getJSONObject(0);
                    JSONObject wind_object = response.getJSONObject("wind"); //객체인 wind 가져오기 (speed, deg)

                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String humidity = String.valueOf(main_object.getDouble("humidity"));
                    String temp_max = String.valueOf(main_object.getDouble("temp_max"));
                    String temp_min = String.valueOf(main_object.getDouble("temp_min"));

                    String weather_icon = String.valueOf(object.getString("icon"));
                    ViewweatherIcon(weather_icon);

                    String wind_speed = String.valueOf(wind_object.getDouble("speed"));
                    String wind_deg = String.valueOf(wind_object.getDouble("deg"));

                    double temp_int = Double.parseDouble(temp);
                    double maxtemp_int = Double.parseDouble(temp_max);
                    double mintemp_int = Double.parseDouble(temp_min);

                    double centi1 = (temp_int - 273.15);
                    double centi2 = (maxtemp_int - 273.15);
                    double centi3 = (mintemp_int - 273.15);
                    int i1 = (int) centi1;
                    int i2 = (int) centi2;
                    int i3 = (int) centi3;

                    now_temp.setText(String.valueOf(i1));
                    max_temp.setText(String.valueOf(i2));
                    min_temp.setText(String.valueOf(i3));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor_current);
    }

    //날씨에 맞는 아이콘 출력
    public void ViewweatherIcon(String iconcode) {
        switch (iconcode) {
            case "01d": //clear sky day
                WeatherIconImg.setImageResource(R.drawable.weather_sun);
                break;
            case "02d": //few clouds day
                WeatherIconImg.setImageResource(R.drawable.weather_few_clouds_sun);
                break;
            case "03d": //scattered clouds day
                WeatherIconImg.setImageResource(R.drawable.weather_clouds);
                break;
            case "04d": //broken clouds day
                WeatherIconImg.setImageResource(R.drawable.weather_broken_clouds);
                break;
            case "09d": //shower rain day
                WeatherIconImg.setImageResource(R.drawable.weather_shower_rain);
                break;
            case "10d": //rain day
                WeatherIconImg.setImageResource(R.drawable.weather_rain_day);
                break;
            case "11d": //thunderstorm day
                WeatherIconImg.setImageResource(R.drawable.weather_thuderstorm);
                break;
            case "13d": //snow day
                WeatherIconImg.setImageResource(R.drawable.weather_snow);
                break;
            case "50d": //mist day
                WeatherIconImg.setImageResource(R.drawable.weather_mist);
                break;
            case "01n": //clear sky night
                WeatherIconImg.setImageResource(R.drawable.weather_night);
                break;
            case "02n": //few clouds night
                WeatherIconImg.setImageResource(R.drawable.weather_few_clouds_nignt);
                break;
            case "03n": //scattered clouds night
                WeatherIconImg.setImageResource(R.drawable.weather_clouds);
                break;
            case "04n": //broken clouds night
                WeatherIconImg.setImageResource(R.drawable.weather_broken_clouds);
                break;
            case "09n": //shower rain night
                WeatherIconImg.setImageResource(R.drawable.weather_shower_rain);
                break;
            case "10n": //rain night
                WeatherIconImg.setImageResource(R.drawable.weather_rain_night);
                break;
            case "11n": //thunderstorm night
                WeatherIconImg.setImageResource(R.drawable.weather_thuderstorm);
                break;
            case "13n": //snow night
                WeatherIconImg.setImageResource(R.drawable.weather_snow);
                break;
            case "50n": //mist night
                WeatherIconImg.setImageResource(R.drawable.weather_mist);
                break;
        }
    }

}