package com.example.doitien;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.*;

public class MainActivity extends AppCompatActivity {
    Item[] items;
    String[] items1;
    List<RssFeedModel> FeedModelList = new ArrayList<>();
    String mFeedTitle;
    String mFeedLink;
    String mFeedDescription;
    int idFrom = 0;
    int idTo = 0;
    float ExchangeRate = 0;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean flag = false;
        /*new ReadRss().execute("https://usd.fxexchangerate.com/rss.xml");
        try {
            SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        prepareDate();
        //preparDate1();
        Spinner SelectFrom = findViewById(R.id.SelectFrom);
        Spinner SelectTo = findViewById(R.id.SelectTo);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SelectFrom.setAdapter(aa);
        SelectTo.setAdapter(aa);
        String url = items[idFrom].getLink();
        new ReadRss().execute(url);
        Button btnChuyen = findViewById(R.id.btnChuyen);
        Button btnDatLai = findViewById(R.id.btnDatLai);
        EditText edtFrom = findViewById(R.id.edtFrom);
        EditText edtTo = findViewById(R.id.edtTo);
        btnChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    edtFrom.getText();
                } catch (Exception e)
                {
                    edtFrom.setText("0");
                }
                idFrom = SelectFrom.getSelectedItemPosition();
                idTo = SelectTo.getSelectedItemPosition();
                //Log.e("ID",String.valueOf(idFrom));
                //Log.e("ID", String.valueOf(idTo));
                // String url = items[idFrom].getLink();
                //Log.e("URL1", url);
                // new ReadRss().execute(url);
                Log.e("Rss Size", String.valueOf(FeedModelList.size()));
                ExchangeRate = getExchangeRate(idFrom, idTo, FeedModelList, items, 0);
                Log.e("Exchange Rate", String.valueOf(ExchangeRate));
                String sfrom = (String) edtFrom.getText().toString();
                if (sfrom.equals(""))
                {
                    sfrom="0";
                    edtFrom.setText("0");
                }
                float from = Float.valueOf(sfrom);
                float to = from * ExchangeRate;
                edtTo.setText(String.valueOf(to));
                ///btnChuyen.setEnabled(false);
                //FeedModelList = new ArrayList<>();
            }
        });
        btnDatLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFrom.setText("0");
                edtTo.setText("0");
                idFrom = 0;
                idTo = 0;
                ExchangeRate = 0;
                btnChuyen.setEnabled(true);

            }
        });
        SelectFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                btnChuyen.setEnabled(false);
                int i = SelectFrom.getSelectedItemPosition();
                String url = items[i].getLink();
                new ReadRss().execute(url);
                btnChuyen.setText("Loading");
                if (FeedModelList.size() > 0)
                {
                    btnChuyen.setEnabled(true);
                    btnChuyen.setText("Chuyển đổi");
                }
                edtTo.setText("0");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int i = 0;
                String url = items[i].getLink();
                new ReadRss().execute(url);
                btnChuyen.setEnabled(false);
            }

        });

    }

    public void preparDate1() {
        items = new Item[FeedModelList.size()];
        Log.e("PrepareDate1 ", String.valueOf(FeedModelList.size()));
        for (int i = 0; i < FeedModelList.size(); i++) {
            String name;
            name = FeedModelList.get(i).getTitle();
            int k = name.indexOf("/");
            Log.e("ID /", String.valueOf(k));
            String link = FeedModelList.get(i).getLink() + "rss.xml";
            items[i ] = new Item(name.substring(k + 1), link);
        }
        items1 = new String[FeedModelList.size() - 1];
        for (int i = 0; i < FeedModelList.size() - 1; i++) {
            items1[i] = items[i].getName();
        }
        for (int i = 0; i <= FeedModelList.size(); i++)
        {
            if (i==FeedModelList.size()) break;
            System.out.println("items["+i+"] = new Item(" +"\""+items[i].getName()+"\",\""+items[i+1].getLink()+"\");");
        }
    }


    public void prepareDate() {
        items = new Item[160];
        items[0] = new Item("Australian Dollar(AUD)","https://aud.fxexchangerate.com/rss.xml");
        items[1] = new Item("Albanian Lek(ALL)","https://all.fxexchangerate.com/rss.xml");
        items[2] = new Item("Algerian Dinar(DZD)","https://dzd.fxexchangerate.com/rss.xml");
        items[3] = new Item("Argentine Peso(ARS)","https://ars.fxexchangerate.com/rss.xml");
        items[4] = new Item("Aruba Florin(AWG)","https://awg.fxexchangerate.com/rss.xml");
        items[5] = new Item("British Pound(GBP)","https://gbp.fxexchangerate.com/rss.xml");
        items[6] = new Item("Bahamian Dollar(BSD)","https://bsd.fxexchangerate.com/rss.xml");
        items[7] = new Item("Bahraini Dinar(BHD)","https://bhd.fxexchangerate.com/rss.xml");
        items[8] = new Item("Bangladesh Taka(BDT)","https://bdt.fxexchangerate.com/rss.xml");
        items[9] = new Item("Barbados Dollar(BBD)","https://bbd.fxexchangerate.com/rss.xml");
        items[10] = new Item("Belarus Ruble(BYR)","https://byr.fxexchangerate.com/rss.xml");
        items[11] = new Item("Belize Dollar(BZD)","https://bzd.fxexchangerate.com/rss.xml");
        items[12] = new Item("Bermuda Dollar(BMD)","https://bmd.fxexchangerate.com/rss.xml");
        items[13] = new Item("Bhutan Ngultrum(BTN)","https://btn.fxexchangerate.com/rss.xml");
        items[14] = new Item("Bolivian Boliviano(BOB)","https://bob.fxexchangerate.com/rss.xml");
        items[15] = new Item("Botswana Pula(BWP)","https://bwp.fxexchangerate.com/rss.xml");
        items[16] = new Item("Brazilian Real(BRL)","https://brl.fxexchangerate.com/rss.xml");
        items[17] = new Item("Brunei Dollar(BND)","https://bnd.fxexchangerate.com/rss.xml");
        items[18] = new Item("Bulgarian Lev(BGN)","https://bgn.fxexchangerate.com/rss.xml");
        items[19] = new Item("Burundi Franc(BIF)","https://bif.fxexchangerate.com/rss.xml");
        items[20] = new Item("Canadian Dollar(CAD)","https://cad.fxexchangerate.com/rss.xml");
        items[21] = new Item("Chinese Yuan(CNY)","https://cny.fxexchangerate.com/rss.xml");
        items[22] = new Item("Cambodia Riel(KHR)","https://khr.fxexchangerate.com/rss.xml");
        items[23] = new Item("Cape Verde Escudo(CVE)","https://cve.fxexchangerate.com/rss.xml");
        items[24] = new Item("Cayman Islands Dollar(KYD)","https://kyd.fxexchangerate.com/rss.xml");
        items[25] = new Item("CFA Franc (BCEAO)(XOF)","https://xof.fxexchangerate.com/rss.xml");
        items[26] = new Item("CFA Franc (BEAC)(XAF)","https://xaf.fxexchangerate.com/rss.xml");
        items[27] = new Item("Chilean Peso(CLP)","https://clp.fxexchangerate.com/rss.xml");
        items[28] = new Item("Colombian Peso(COP)","https://cop.fxexchangerate.com/rss.xml");
        items[29] = new Item("Comoros Franc(KMF)","https://kmf.fxexchangerate.com/rss.xml");
        items[30] = new Item("Costa Rica Colon(CRC)","https://crc.fxexchangerate.com/rss.xml");
        items[31] = new Item("Croatian Kuna(HRK)","https://hrk.fxexchangerate.com/rss.xml");
        items[32] = new Item("Cuban Peso(CUP)","https://cup.fxexchangerate.com/rss.xml");
        items[33] = new Item("Czech Koruna(CZK)","https://czk.fxexchangerate.com/rss.xml");
        items[34] = new Item("Euro(EUR)","https://eur.fxexchangerate.com/rss.xml");
        items[35] = new Item("Danish Krone(DKK)","https://dkk.fxexchangerate.com/rss.xml");
        items[36] = new Item("Djibouti Franc(DJF)","https://djf.fxexchangerate.com/rss.xml");
        items[37] = new Item("Dominican Peso(DOP)","https://dop.fxexchangerate.com/rss.xml");
        items[38] = new Item("East Caribbean Dollar(XCD)","https://xcd.fxexchangerate.com/rss.xml");
        items[39] = new Item("Egyptian Pound(EGP)","https://egp.fxexchangerate.com/rss.xml");
        items[40] = new Item("El Salvador Colon(SVC)","https://svc.fxexchangerate.com/rss.xml");
        items[41] = new Item("Estonian Kroon(EEK)","https://eek.fxexchangerate.com/rss.xml");
        items[42] = new Item("Ethiopian Birr(ETB)","https://etb.fxexchangerate.com/rss.xml");
        items[43] = new Item("Falkland Islands Pound(FKP)","https://fkp.fxexchangerate.com/rss.xml");
        items[44] = new Item("Fiji Dollar(FJD)","https://fjd.fxexchangerate.com/rss.xml");
        items[45] = new Item("Hong Kong Dollar(HKD)","https://hkd.fxexchangerate.com/rss.xml");
        items[46] = new Item("Indonesian Rupiah(IDR)","https://idr.fxexchangerate.com/rss.xml");
        items[47] = new Item("Indian Rupee(INR)","https://inr.fxexchangerate.com/rss.xml");
        items[48] = new Item("Gambian Dalasi(GMD)","https://gmd.fxexchangerate.com/rss.xml");
        items[49] = new Item("Guatemala Quetzal(GTQ)","https://gtq.fxexchangerate.com/rss.xml");
        items[50] = new Item("Guinea Franc(GNF)","https://gnf.fxexchangerate.com/rss.xml");
        items[51] = new Item("Guyana Dollar(GYD)","https://gyd.fxexchangerate.com/rss.xml");
        items[52] = new Item("Haiti Gourde(HTG)","https://htg.fxexchangerate.com/rss.xml");
        items[53] = new Item("Honduras Lempira(HNL)","https://hnl.fxexchangerate.com/rss.xml");
        items[54] = new Item("Hungarian Forint(HUF)","https://huf.fxexchangerate.com/rss.xml");
        items[55] = new Item("Iceland Krona(ISK)","https://isk.fxexchangerate.com/rss.xml");
        items[56] = new Item("Iran Rial(IRR)","https://irr.fxexchangerate.com/rss.xml");
        items[57] = new Item("Iraqi Dinar(IQD)","https://iqd.fxexchangerate.com/rss.xml");
        items[58] = new Item("Israeli Shekel(ILS)","https://ils.fxexchangerate.com/rss.xml");
        items[59] = new Item("Japanese Yen(JPY)","https://jpy.fxexchangerate.com/rss.xml");
        items[60] = new Item("Jamaican Dollar(JMD)","https://jmd.fxexchangerate.com/rss.xml");
        items[61] = new Item("Jordanian Dinar(JOD)","https://jod.fxexchangerate.com/rss.xml");
        items[62] = new Item("Kazakhstan Tenge(KZT)","https://kzt.fxexchangerate.com/rss.xml");
        items[63] = new Item("Kenyan Shilling(KES)","https://kes.fxexchangerate.com/rss.xml");
        items[64] = new Item("Korean Won(KRW)","https://krw.fxexchangerate.com/rss.xml");
        items[65] = new Item("Kuwaiti Dinar(KWD)","https://kwd.fxexchangerate.com/rss.xml");
        items[66] = new Item("Lao Kip(LAK)","https://lak.fxexchangerate.com/rss.xml");
        items[67] = new Item("Latvian Lat(LVL)","https://lvl.fxexchangerate.com/rss.xml");
        items[68] = new Item("Lebanese Pound(LBP)","https://lbp.fxexchangerate.com/rss.xml");
        items[69] = new Item("Lesotho Loti(LSL)","https://lsl.fxexchangerate.com/rss.xml");
        items[70] = new Item("Liberian Dollar(LRD)","https://lrd.fxexchangerate.com/rss.xml");
        items[71] = new Item("Libyan Dinar(LYD)","https://lyd.fxexchangerate.com/rss.xml");
        items[72] = new Item("Lithuanian Lita(LTL)","https://ltl.fxexchangerate.com/rss.xml");
        items[73] = new Item("Macau Pataca(MOP)","https://mop.fxexchangerate.com/rss.xml");
        items[74] = new Item("Macedonian Denar(MKD)","https://mkd.fxexchangerate.com/rss.xml");
        items[75] = new Item("Malawi Kwacha(MWK)","https://mwk.fxexchangerate.com/rss.xml");
        items[76] = new Item("Malaysian Ringgit(MYR)","https://myr.fxexchangerate.com/rss.xml");
        items[77] = new Item("Maldives Rufiyaa(MVR)","https://mvr.fxexchangerate.com/rss.xml");
        items[78] = new Item("Mauritania Ougulya(MRO)","https://mro.fxexchangerate.com/rss.xml");
        items[79] = new Item("Mauritius Rupee(MUR)","https://mur.fxexchangerate.com/rss.xml");
        items[80] = new Item("Mexican Peso(MXN)","https://mxn.fxexchangerate.com/rss.xml");
        items[81] = new Item("Moldovan Leu(MDL)","https://mdl.fxexchangerate.com/rss.xml");
        items[82] = new Item("Mongolian Tugrik(MNT)","https://mnt.fxexchangerate.com/rss.xml");
        items[83] = new Item("Moroccan Dirham(MAD)","https://mad.fxexchangerate.com/rss.xml");
        items[84] = new Item("Myanmar Kyat(MMK)","https://mmk.fxexchangerate.com/rss.xml");
        items[85] = new Item("Namibian Dollar(NAD)","https://nad.fxexchangerate.com/rss.xml");
        items[86] = new Item("Nepalese Rupee(NPR)","https://npr.fxexchangerate.com/rss.xml");
        items[87] = new Item("Neth Antilles Guilder(ANG)","https://ang.fxexchangerate.com/rss.xml");
        items[88] = new Item("New Zealand Dollar(NZD)","https://nzd.fxexchangerate.com/rss.xml");
        items[89] = new Item("Nicaragua Cordoba(NIO)","https://nio.fxexchangerate.com/rss.xml");
        items[90] = new Item("Nigerian Naira(NGN)","https://ngn.fxexchangerate.com/rss.xml");
        items[91] = new Item("North Korean Won(KPW)","https://kpw.fxexchangerate.com/rss.xml");
        items[92] = new Item("Norwegian Krone(NOK)","https://nok.fxexchangerate.com/rss.xml");
        items[93] = new Item("Omani Rial(OMR)","https://omr.fxexchangerate.com/rss.xml");
        items[94] = new Item("Pacific Franc(XPF)","https://xpf.fxexchangerate.com/rss.xml");
        items[95] = new Item("Pakistani Rupee(PKR)","https://pkr.fxexchangerate.com/rss.xml");
        items[96] = new Item("Panama Balboa(PAB)","https://pab.fxexchangerate.com/rss.xml");
        items[97] = new Item("Papua New Guinea Kina(PGK)","https://pgk.fxexchangerate.com/rss.xml");
        items[98] = new Item("Paraguayan Guarani(PYG)","https://pyg.fxexchangerate.com/rss.xml");
        items[99] = new Item("Peruvian Nuevo Sol(PEN)","https://pen.fxexchangerate.com/rss.xml");
        items[100] = new Item("Philippine Peso(PHP)","https://php.fxexchangerate.com/rss.xml");
        items[101] = new Item("Polish Zloty(PLN)","https://pln.fxexchangerate.com/rss.xml");
        items[102] = new Item("Qatar Rial(QAR)","https://qar.fxexchangerate.com/rss.xml");
        items[103] = new Item("Romanian New Leu(RON)","https://ron.fxexchangerate.com/rss.xml");
        items[104] = new Item("Russian Rouble(RUB)","https://rub.fxexchangerate.com/rss.xml");
        items[105] = new Item("Rwanda Franc(RWF)","https://rwf.fxexchangerate.com/rss.xml");
        items[106] = new Item("Swiss Franc(CHF)","https://chf.fxexchangerate.com/rss.xml");
        items[107] = new Item("Samoa Tala(WST)","https://wst.fxexchangerate.com/rss.xml");
        items[108] = new Item("Sao Tome Dobra(STD)","https://std.fxexchangerate.com/rss.xml");
        items[109] = new Item("Saudi Arabian Riyal(SAR)","https://sar.fxexchangerate.com/rss.xml");
        items[110] = new Item("Seychelles Rupee(SCR)","https://scr.fxexchangerate.com/rss.xml");
        items[111] = new Item("Sierra Leone Leone(SLL)","https://sll.fxexchangerate.com/rss.xml");
        items[112] = new Item("Singapore Dollar(SGD)","https://sgd.fxexchangerate.com/rss.xml");
        items[113] = new Item("Slovak Koruna(SKK)","https://skk.fxexchangerate.com/rss.xml");
        items[114] = new Item("Solomon Islands Dollar(SBD)","https://sbd.fxexchangerate.com/rss.xml");
        items[115] = new Item("Somali Shilling(SOS)","https://sos.fxexchangerate.com/rss.xml");
        items[116] = new Item("South African Rand(ZAR)","https://zar.fxexchangerate.com/rss.xml");
        items[117] = new Item("Sri Lanka Rupee(LKR)","https://lkr.fxexchangerate.com/rss.xml");
        items[118] = new Item("St Helena Pound(SHP)","https://shp.fxexchangerate.com/rss.xml");
        items[119] = new Item("Sudanese Pound(SDG)","https://sdg.fxexchangerate.com/rss.xml");
        items[120] = new Item("Swaziland Lilageni(SZL)","https://szl.fxexchangerate.com/rss.xml");
        items[121] = new Item("Swedish Krona(SEK)","https://sek.fxexchangerate.com/rss.xml");
        items[122] = new Item("Syrian Pound(SYP)","https://syp.fxexchangerate.com/rss.xml");
        items[123] = new Item("United States Dollar(USD)","https://usd.fxexchangerate.com/rss.xml");
        items[124] = new Item("Thai Baht(THB)","https://thb.fxexchangerate.com/rss.xml");
        items[125] = new Item("Turkish Lira(TRY)","https://try.fxexchangerate.com/rss.xml");
        items[126] = new Item("Taiwan Dollar(TWD)","https://twd.fxexchangerate.com/rss.xml");
        items[127] = new Item("Tanzanian Shilling(TZS)","https://tzs.fxexchangerate.com/rss.xml");
        items[128] = new Item("Tongan paʻanga(TOP)","https://top.fxexchangerate.com/rss.xml");
        items[129] = new Item("Trinidad Tobago Dollar(TTD)","https://ttd.fxexchangerate.com/rss.xml");
        items[130] = new Item("Tunisian Dinar(TND)","https://tnd.fxexchangerate.com/rss.xml");
        items[131] = new Item("UAE Dirham(AED)","https://aed.fxexchangerate.com/rss.xml");
        items[132] = new Item("Ugandan Shilling(UGX)","https://ugx.fxexchangerate.com/rss.xml");
        items[133] = new Item("Ukraine Hryvnia(UAH)","https://uah.fxexchangerate.com/rss.xml");
        items[134] = new Item("Uruguayan New Peso(UYU)","https://uyu.fxexchangerate.com/rss.xml");
        items[135] = new Item("Vanuatu Vatu(VUV)","https://vuv.fxexchangerate.com/rss.xml");
        items[136] = new Item("Vietnam Dong(VND)","https://vnd.fxexchangerate.com/rss.xml");
        items[137] = new Item("Yemen Riyal(YER)","https://yer.fxexchangerate.com/rss.xml");
        items[138] = new Item("Zambian Kwacha(ZMK)","https://zmk.fxexchangerate.com/rss.xml");
        items[139] = new Item("Zimbabwe dollar(ZWD)","https://zwd.fxexchangerate.com/rss.xml");
        items[140] = new Item("Venezuelan Bolivar(VEF)","https://vef.fxexchangerate.com/rss.xml");
        items[141] = new Item("Uzbekistan Sum(UZS)","https://uzs.fxexchangerate.com/rss.xml");
        items[142] = new Item("Kyrgyzstan Som(KGS)","https://kgs.fxexchangerate.com/rss.xml");
        items[143] = new Item("Ghanaian Cedi(GHS)","https://ghs.fxexchangerate.com/rss.xml");
        items[144] = new Item("Belarusian ruble(BYN)","https://byn.fxexchangerate.com/rss.xml");
        items[145] = new Item("Afghan afghani(AFN)","https://afn.fxexchangerate.com/rss.xml");
        items[146] = new Item("Angolan kwanza(AOA)","https://aoa.fxexchangerate.com/rss.xml");
        items[147] = new Item("Armenian dram(AMD)","https://amd.fxexchangerate.com/rss.xml");
        items[148] = new Item("Azerbaijani manat(AZN)","https://azn.fxexchangerate.com/rss.xml");
        items[149] = new Item("Convertible mark(BAM)","https://bam.fxexchangerate.com/rss.xml");
        items[150] = new Item("Congolese franc(CDF)","https://cdf.fxexchangerate.com/rss.xml");
        items[151] = new Item("Eritrean nakfa(ERN)","https://ern.fxexchangerate.com/rss.xml");
        items[152] = new Item("Georgian lari(GEL)","https://gel.fxexchangerate.com/rss.xml");
        items[153] = new Item("Malagasy ariary(MGA)","https://mga.fxexchangerate.com/rss.xml");
        items[154] = new Item("Mozambican metical(MZN)","https://mzn.fxexchangerate.com/rss.xml");
        items[155] = new Item("Serbian dinar(RSD)","https://rsd.fxexchangerate.com/rss.xml");
        items[156] = new Item("Surinamese dollar(SRD)","https://srd.fxexchangerate.com/rss.xml");
        items[157] = new Item("Tajikistani somoni(TJS)","https://tjs.fxexchangerate.com/rss.xml");
        items[158] = new Item("Turkmenistan manat(TMT)","https://tmt.fxexchangerate.com/rss.xml");
        items[159] = new Item("Zambian kwacha(ZMW)","https://zmw.fxexchangerate.com/rss.xml");

        items1 = new String[160];
        for (int i = 0; i < 160; i++) {
            items1[i] = items[i].getName();
        }
    }

    private float getExchangeRate(int idFrom, int idTo, List<RssFeedModel> FeedModelList, Item[] items, int i) {
        String fullTittle, des = "", ExchangeRate = "0";
        float Exchange = 0;
        String nameFrom = items[idFrom].getName();
        String nameTo = items[idTo].getName();
        fullTittle = nameFrom + "/" + nameTo;
        Log.e("Full Tittle: ", fullTittle);
        Log.e("i", String.valueOf(i));
        while (1 == 1) {
            if (i >= 160) break;
            if (FeedModelList.get(i).getTitle().equals(fullTittle)) {
                des = FeedModelList.get(i + 1).getDescription();
                Log.e("i", String.valueOf(i));
                break;
            }
            i++;
        }
        try {
            int beginIndex = des.indexOf("=");
            int endIndex = des.indexOf(" ", beginIndex + 2);
            ExchangeRate = des.substring(beginIndex + 1, endIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Exchange = Float.valueOf(ExchangeRate);
        return Exchange;
    }

    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    } else {
                        mFeedTitle = title;
                        mFeedLink = link;
                        mFeedDescription = description;
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }

    private class ReadRss extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                Log.e("URL ReadRss: ", url.toString());
                InputStream inputStream = url.openStream();
                FeedModelList.clear();
                FeedModelList = parseFeed(inputStream);
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}