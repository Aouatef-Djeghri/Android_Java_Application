package com.example.servicebypro.Activities.HelperClasses;

import android.content.Context;

import com.example.servicebypro.R;
import com.example.servicebypro.Remote.Models.Wilaya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int MAX_STEP = 9;
    public final static int LOCATION_REQUEST_CODE = 23;
    public static final float DEFAULT_ZOOM = 15f;

    public static final int REQUEST_EXTERNAL_STORAGE = 1;

    public static final ArrayList<Wilaya> getWilayaAndCommunes(Context context){

        List<String> arrayList_wilaya = Arrays.asList(context.getResources().getStringArray(R.array.wilaya));
        ArrayList<Wilaya> wilayas = new ArrayList<>();
        wilayas.add(new Wilaya(arrayList_wilaya.get(1),1, (float) 26.488816,(float)-1.358244, Arrays.asList(context.getResources().getStringArray(R.array.communes_1))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(2),2,(float)36.20342,(float) 1.26807, Arrays.asList(context.getResources().getStringArray(R.array.communes_2))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(3),3,(float)33.750441,(float)2.643109 , Arrays.asList(context.getResources().getStringArray(R.array.communes_3))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(4),4,(float)35.810581,(float)7.018418, Arrays.asList(context.getResources().getStringArray(R.array.communes_4))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(5),5,(float)35.338429,(float)5.731545, Arrays.asList(context.getResources().getStringArray(R.array.communes_5))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(6),6,(float)36.751178,(float) 5.064369, Arrays.asList(context.getResources().getStringArray(R.array.communes_6))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(7),7,(float) 34.784564,(float)5.812435, Arrays.asList(context.getResources().getStringArray(R.array.communes_7))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(8),8,(float)31.385726,(float)-2.011596, Arrays.asList(context.getResources().getStringArray(R.array.communes_8))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(9),9,(float)36.470165,(float)2.828799, Arrays.asList(context.getResources().getStringArray(R.array.communes_9))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(10),10,(float)36.231648,(float)3.908258, Arrays.asList(context.getResources().getStringArray(R.array.communes_10))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(11),11,(float)24.375344,(float)4.320844, Arrays.asList(context.getResources().getStringArray(R.array.communes_11))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(12),12,(float)35.124945,(float)7.901174, Arrays.asList(context.getResources().getStringArray(R.array.communes_12))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(13),13,(float)34.881789,(float)-1.316699, Arrays.asList(context.getResources().getStringArray(R.array.communes_13))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(14),14,(float)34.894758,(float)1.594579, Arrays.asList(context.getResources().getStringArray(R.array.communes_14))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(15),15,(float)36.681618,(float)4.237186, Arrays.asList(context.getResources().getStringArray(R.array.communes_15))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(16),16,(float)36.775361,(float)3.060188, Arrays.asList(context.getResources().getStringArray(R.array.communes_16))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(17),17,(float)34.342841,(float)3.217253, Arrays.asList(context.getResources().getStringArray(R.array.communes_17))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(18),18,(float)36.729219,(float)5.960778, Arrays.asList(context.getResources().getStringArray(R.array.communes_18))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(19),19,(float)36.189275,(float)5.403493, Arrays.asList(context.getResources().getStringArray(R.array.communes_19))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(20),20,(float)34.743349,(float) 0.244076, Arrays.asList(context.getResources().getStringArray(R.array.communes_20))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(21),21,(float)36.754512,(float)6.885626, Arrays.asList(context.getResources().getStringArray(R.array.communes_21))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(22),22,(float)34.682268,(float)-0.435755, Arrays.asList(context.getResources().getStringArray(R.array.communes_22))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(23),23,(float) 36.898217,(float) 7.754927, Arrays.asList(context.getResources().getStringArray(R.array.communes_23))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(24),24,(float)36.349164,(float)7.409499, Arrays.asList(context.getResources().getStringArray(R.array.communes_24))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(25),25,(float)36.364519,(float)6.60826, Arrays.asList(context.getResources().getStringArray(R.array.communes_25))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(26),26,(float)35.975205,(float)3.01235, Arrays.asList(context.getResources().getStringArray(R.array.communes_26))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(27),27,(float)36.002692,(float)0.368687, Arrays.asList(context.getResources().getStringArray(R.array.communes_27))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(28),28,(float)35.130021,(float)4.200311, Arrays.asList(context.getResources().getStringArray(R.array.communes_28))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(29),29,(float)35.397839 ,(float)0.24302, Arrays.asList(context.getResources().getStringArray(R.array.communes_29))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(30),30,(float)30.998015,(float)6.766454, Arrays.asList(context.getResources().getStringArray(R.array.communes_30))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(31),31,(float)35.703275,(float)-0.649298, Arrays.asList(context.getResources().getStringArray(R.array.communes_31))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(32),32,(float)32.570303,(float)1.125958, Arrays.asList(context.getResources().getStringArray(R.array.communes_32))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(33),33,(float)27.852851,(float)7.818964, Arrays.asList(context.getResources().getStringArray(R.array.communes_33))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(34),34,(float)36.095506,(float)4.6611, Arrays.asList(context.getResources().getStringArray(R.array.communes_34))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(35),35,(float)36.735803,(float)3.616305, Arrays.asList(context.getResources().getStringArray(R.array.communes_35))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(36),36,(float)36.671356,(float)8.070134, Arrays.asList(context.getResources().getStringArray(R.array.communes_36))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(37),37,(float)27.543907,(float)-6.240054, Arrays.asList(context.getResources().getStringArray(R.array.communes_37))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(38),38,(float)35.785898,(float)1.834096, Arrays.asList(context.getResources().getStringArray(R.array.communes_38))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(39),39,(float)33.215441,(float)7.155321, Arrays.asList(context.getResources().getStringArray(R.array.communes_39))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(40),40,(float)34.913346,(float)6.905943, Arrays.asList(context.getResources().getStringArray(R.array.communes_40))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(41),41,(float) 36.137868,(float)7.826243, Arrays.asList(context.getResources().getStringArray(R.array.communes_41))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(42),42,(float) 36.527274,(float)2.168369, Arrays.asList(context.getResources().getStringArray(R.array.communes_42))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(43),43,(float)36.438022,(float)6.247579, Arrays.asList(context.getResources().getStringArray(R.array.communes_43))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(44),44,(float)36.158684, (float)2.084282, Arrays.asList(context.getResources().getStringArray(R.array.communes_44))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(45),45,(float)36.095506,(float)-0.815196, Arrays.asList(context.getResources().getStringArray(R.array.communes_45))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(46),46,(float)35.365047,(float)-0.945281, Arrays.asList(context.getResources().getStringArray(R.array.communes_46))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(47),47,(float)30.979872,(float)3.099085, Arrays.asList(context.getResources().getStringArray(R.array.communes_47))));
        wilayas.add(new Wilaya(arrayList_wilaya.get(48),48,(float)35.836319,(float)0.911854, Arrays.asList(context.getResources().getStringArray(R.array.communes_48))));


        return  wilayas;
    }

}
