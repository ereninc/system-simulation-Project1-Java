import java.util.Random;
import java.util.Queue;
import java.util.*;
/*
notlar(dersteki degiskenler)
lq-yükleme kuyrugu
wq-tarti kuyrugu
LQ(t) yukleyici kuyrugundaki kamyon sayisi
L(t) yuklemedeki kamyon sayisi
WQ(t) agirlik sirasindaki kamyon sayisi
W(t) tartimdaki kamyon sayisi
EL endload-yuklemebitimi
EW endweighing-tartim bitimi
*/
class Event
{
    final int time;
    final int truck;
    final String type;
    public Event(String _type,int _time,int _truck){
        type=_type;
        time=_time;
        truck=_truck;
    }
    
    public String getType(){
        return type;
    }
    
    public int getTime(){
        return time;
    }
    
    public int getTruck(){
        return truck;
    }
    
    public void Yazdir(){
        System.out.println(" ("+getType()+", "+getTime()+", Kamyon"+getTruck()+")");
    }
}

class GelecekOlaylarListesi{
    Event event[]=new Event[800];
    int size;
    GelecekOlaylarListesi(){
        size=0;
    }
    
    public void OlayEkle(Event ev){
        if(size<200){
            event[size]=ev; 
            size++; 
        }
    }

    public Event OlaySil(){ 
        int sayac = 0;
        int index =0;
        int temp= 0;
        while(sayac < size){
            if(event[sayac]!=null){
                temp=event[sayac].time;break;
            }
            sayac++;
        }
        sayac=0;
        while(sayac < size){
            if(event[sayac]!=null && event[sayac].time<=temp){
                temp=event[sayac].time;
                index=sayac;
            }
            sayac++;
        }
        Event bitenOlayiSil=new Event(event[index].getType(),event[index].getTime(),event[index].getTruck());
        event[index]=null;
        return bitenOlayiSil;
    }
    
    public void Yazdir(){
        int sayac = 0;
        while(sayac < 800){
            if(event[sayac]!=null){
                event[sayac].Yazdir();
            }
            sayac++;
        }
    }
}

class KuyrukSinif{
    int q[],f,r;
    public KuyrukSinif(){
        q=new int[800];
        f=0;
    }
    
    public void SirayaEkle(int truck){
        q[f]=truck;
        f++;
    }
    
    public int SiradanKaldir(){
        for(int i=0;i<10;i++){
            if(q[i]!=0){
                int ind=q[i];
                q[i]=0;
                return ind;
            }
        }
        return 0;
    }
    
    public void Yazdir(){
        for(int i=0;i<100;i++){
            if(q[i]!=0){
                System.out.print(" Kamyon"+q[i]);
            }
        }  
    }
}

class RandomSeed{
    //yükleme zamanı var, tartım zamanı var, yolculuk zamanı var.
    public static int YuklemeZamani(){
        //1-3'de 5, 4-8'de 10, 9-0'da 15
        int seed;
        Random rnd = new Random(10);
        seed = rnd.nextInt();
        if(seed < 3){
            return 5;
        }
        if(seed < 8){
            return 10;
        }
        return 15;
    }
	
    public static int TartiZamani(){
	//1-7 gelirse 12, 8-0 gelirse 16
        int seed;
	Random rnd = new Random(10);
	seed = rnd.nextInt();
	if(seed < 7){
            return 12;
	}
	return 16;
    }
	
    public static int YoldaGecenZaman(){
    //1-4 gelirse 40, 5-7 gelirse 60, 8-9 gelirse 80, 0 gelirse 100
    	int seed;
    	Random rnd = new Random(10);
    	seed = rnd.nextInt();
    	if(seed < 4){
            return 40;
	}
	if(seed < 8){
            return 60;
	}
	if(seed < 9){
            return 80;
	}
	return 100;
    }
}

class Damper{
    public static int ct,loadq,load,weighq,weigh,fel,cumulative,statistic;//dersteki sistem tablosundaki sutunlar
    public static void main(String args[]){
        GelecekOlaylarListesi list=new GelecekOlaylarListesi();
        KuyrukSinif lq=new KuyrukSinif() ;
        KuyrukSinif wq=new KuyrukSinif() ;
        EventGenerate(list,lq,wq);
    }
    
    public static void EventGenerate(GelecekOlaylarListesi list,KuyrukSinif lq,KuyrukSinif wq){
        Event endevent=new Event("Bitis",10,0);
        list.OlayEkle(endevent);
        //manuel giris
        loadq=3;
        weighq=0;
        load=2;
        weigh=1;
        ct=0;
        lq.SirayaEkle(4);
        lq.SirayaEkle(5);
        lq.SirayaEkle(6);
        list.OlayEkle(new Event("Yukleme_Bitis",5,3));
        list.OlayEkle(new Event("Yukleme_Bitis",10,2));
        list.OlayEkle(new Event("Tartim_Bitis",12,1));
        Baslangic(list,lq,wq);
    }

    public static void Baslangic(GelecekOlaylarListesi list,KuyrukSinif lq,KuyrukSinif wq){
        System.out.println(" _________________________________________" );
        System.out.println(" Clock(t) : "+ct);
        System.out.println(" Simulasyon Tablosu");
        System.out.println(" -----------------------------------------");
        System.out.println(" LQ(t): "+loadq+" - WQ(t): "+weighq+" - L(t): "+load+" - W(t): "+weigh);
        System.out.println(" Yukleme Kuyrugu :");
        lq.Yazdir();
        System.out.println();
        System.out.println(" Tartilma Kuyrugu :");
        wq.Yazdir();
        System.out.println();
        System.out.println(" GelecekOlaylarListesi: ");
        list.Yazdir();
        Event siradaki=list.OlaySil();
        ct=siradaki.time;
        if(siradaki.type.equals("Yukleme_Bitis")){
            Yukleme_Bitis(list,siradaki,lq,wq);
        }
        if(siradaki.type.equals("Tartim_Bitis")){
            Tartim_Bitis(list,siradaki,lq,wq);
        }
        if(siradaki.type.equals("Bitis")){
            return;
        }
        Baslangic(list,lq,wq);
    }
    
    public static void Yuklemeye_Varis(GelecekOlaylarListesi list,Event siradaki,KuyrukSinif lq,KuyrukSinif wq)
    {
        int sonrakineGecenZaman;
        if(load<2){
            load++;
            sonrakineGecenZaman=ct+RandomSeed.YuklemeZamani();
            list.OlayEkle(new Event("Yukleme_Bitis",sonrakineGecenZaman,siradaki.truck));
        }
        if(load==2){
            loadq++;
            lq.SirayaEkle(siradaki.truck);
        }
    }
    
    public static void Yukleme_Bitis(GelecekOlaylarListesi list,Event siradaki,KuyrukSinif lq,KuyrukSinif wq){
        int sonrakineGecenZaman;
        if(weigh<1){
            weigh++;
            load--;
            sonrakineGecenZaman=ct+RandomSeed.TartiZamani();
            list.OlayEkle(new Event("Tartim_Bitis",sonrakineGecenZaman,siradaki.truck));  
        }
        if(weigh==1){
            weighq++;
            load--;
            wq.SirayaEkle(siradaki.truck);
        }
        if(loadq!=0){
            load++;
            loadq--;
            sonrakineGecenZaman=ct+RandomSeed.YuklemeZamani();
            list.OlayEkle(new Event("Yukleme_Bitis",sonrakineGecenZaman,lq.SiradanKaldir()));
        }
    }
    
    public static void Tartim_Bitis(GelecekOlaylarListesi list,Event siradaki,KuyrukSinif lq,KuyrukSinif wq){
        int sonrakineGecenZaman;
        weigh--;
        sonrakineGecenZaman=ct+RandomSeed.YoldaGecenZaman();
        list.OlayEkle(new Event("Yuklemeye_Varis",sonrakineGecenZaman,siradaki.truck));
        if(weighq!=0){
            weigh++;
            weighq--;
            sonrakineGecenZaman=ct+RandomSeed.TartiZamani();
            list.OlayEkle(new Event("Tartim_Bitis",sonrakineGecenZaman,wq.SiradanKaldir()));
        }
    }
}