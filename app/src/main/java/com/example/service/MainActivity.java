package com.example.service;


import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
/*
lyoum la7 ne7ke 3an mawdo3 mohem mn l android commponent ma fena nest8ne 3ano le howe mawdo3 l 5adamet 2aw services
sho mno2soud bel services? aymta mnest3mela? keef mnest3mela?sho anwe3a lal srvices?
kel haw l 2as2ele la7 njeweb 3lyha b mawdo3 services

hala2 ne7na 2awl ma balshna bel android 2olna fe 4 commponents asaseye le application l android mawjoud b frame l android le houne:
1-Activitys:kel she mara2 abl howe 3ebra 3an activity ui,fragemnt,recycle kel she mara2 howe activitys
2-Services:le 7a nblesh feh hala2
3-content providers
4-broadcast recivers

hawde houne l 4 commponent ra2seye 2aw 4 2a3mede asaseye mawjoude bel android la aye wa7ad bado yet3emal 2aw bado yefham l android


sho mn2soud bel services?
services 3ebra 3an mokawen mn mokawen l android l asaseye btesta5dam la tanfez 3amlyet bel 5alfeye in backgroud ya3ne bdoun ui bedoun user interface thread sho ya3ne heda l kalem:
kel l applications le t3emlna ma3a abl keen fe 3ana user interface keen 3ana ui l ui heda keen feha buttons,views kona net3emal ma3a 3ashen no3red l bayanet  ma kona nafez code in background l2no ken lezm ykoun fe ui services ejet 3ashen testm7lk tnafez code in background howe w l application tafe mesh darore ykoun l application sha8al  keef ya3ne
exmple:enta l youm badak ta3eml application la tash8el music hala2 law jarabt test3mel anghami malsn application tab3 a8ane enta mesh darore tkoun fet7o  lal application 3ashen tesma3 8oneye momken tsaker l application  w dala l music sha8ale
exmple2:enta badak ta3eml application la ta3eml download la files mn  internet mesh darore dalak fete7 l application 3ashn ta3emel download la she file bekafe msln ykoun fe notification bebyenak l progress taba3 donwload eno adesh sar ma3mlo download
hay exmple kola  3an mawdo3 services 2aw l 5adamet le heye 3ebra 3an commponents btesm7lk btanfez awemer barmajeye sawe2 kenet aseret l mada 2aw tawelet l mada fel 5alfeye bel background bdoun ma te7tej  enak tkoun b de5el l application  2aw fete7 l application w sheyfo edemak
heda 3onsour mohem kteer most5dam kteer  beb3ad l esmple metl le 7kenehoun w fe exmple 8ayroun kteer most5dam fehoun mawdo3 services

hala2 services fe mena anwe3 anwe3 mn 7es ta3amoul ma3a w fe anwa3 mn 7es l classes tab3etha keef ya3ne:

hal2 anwa3 services 2aw l 5adamet mn 7es l 2awlaweye taba3etha 2aw b ma3na tene  eno adesh system bedal m7afez 3lyha fe 3 anwa3:
1-Background Service:heye service bteshte8l bshakl kemel bel background bedoun aye rabet bayna w been l user
2-Foreground Service:service betkoun sha8ale sa7e7 bel background laken btkoun mertebta b 3onsour l user sheyfo b3yno metl l notification sa7 enta l music betkoun sha8ale bel background laken bekoun fe 3andak bel notification channel 2aw bel notification bar fo2 bekoun fe 3andak notification 5asa b 2esm l music btesm7lak eno twa2efa btesm7lk eno ta3mel next lal music le 7eda,previous etc .. fa hay mnsameha foreground service la7 ne7ke 3ala tafasela
3-Bound Service:heye 3ebra 3an service 2aw 5edme mortabeta b activity malsn ya3ne bekoun fe activity sha8ale w l activity hay mortabet feha service kel ma l activity sha8ale l service sha8ale  law l activity sakaret damaret l service la7ala b tsaker btetdamar

haw l 3 anawa3 mn service tab3an haw  l anwa3 m2asame 7asab 2awlaweyet l ta3amoul  l nezam l android system  ma3ha bma3na 7a nshof ba3d heek:
background service:l service le bteshte8l bshakl kemel bel background ya3ne ma bekoun fe notification wala aye rabet bayna w been l user hay service momken system law 7tej space memory mn ram w tar ysaker aye sha8le mafto7a bel system momken yrou7 ysaker service hay le no3a background service laken l 2awlaweye le 2a3la mn l background service heye l foreground service
foreground service:l service le bteshte8l bel background btkoun mortebta b 3onsour mo3yan l user sheyfo b3ayno mnkoun rabtena ne7na b notification mbyen bel status bar 2aw notification bar bshakl eno ma ye2dar l system yel8eha l2no fe3lyan  l user me7teja fa ne7na ka2no mne7kelo eno hay l notification mawjoude l2no l user be2olak hay 2awlaweyeta 3alye ma tsakera la7alk ya system 8er   iza 2oltelak sakera ana sakrta b 2ede mnla7ez eno bel foreground service 2aw b application l music bshakl 3am lama tsha8el l music ma bte2dar tshel l notification ela law wa2ft  music  la7 nshof heda l kalem ba3d heek
Bound service:heye 3ebra 3an service 2aw 5edme mortabeta b activity malsn ya3ne bekoun fe activity sha8ale w l activity hay mortabet feha service kel ma l activity sha8ale l service sha8ale  law l activity sakaret damaret l service la7ala b tsaker btetdamar hay 2awlaweyata 2a3la she l2no btkoun mortebta b 3osnour sha8al bel foreground mortabeta b activity mo3yane  l user byet3emal ma3a

hayda ta2sem l service men7ye ta3amoul system ma3a


hala2 badna ne7ke 3an ta2semet l service mne7eyt ta3amoul ma3 l class:
mnet3emal ne7na ma3 2 clases:
Service and Intent service
kel class mn haw  elo momyezet w 3oyoub la7 nshofa


hala2 2awl she la7 nblesh ntbe2 w nshof 2awl service le heye l background service beste3mel class service w 7a nshof sho l mashekl l mawjoude fe le 5aletna nente2l la class l intent service:
hala2 2awl she 3ashen enshe2 service metl ma kent enshe2 activity nafs she ba3ml new badel ma ekbous activity bekbous service bas teje la tekbousa byefta7lk eno fe naw3en mn service aye wa7de badak le houne naw3en le zakrehoun  be2olak fe service naw3a intent service w fe service law7da
tnyanetoun hawde service tnyanetoun 3emlen extend mn calss service bas lama net3emal ma3 class service la2o eno fe ba3d l eshya momken ta7sen 3lyha fa 3mloha hay ta7senet b class esmo intent service w 7atolna yeh
5alena mabd2yan net3emal ma3 class service l 3ade mesh l intenet service  fa ba3mel heda service w bsameha my service
byes2lak enta w 3am tenshe2a hal badak yeha hay service exported ya3ne hal btesma7 lal applicationet l 5arejye l mawjoude 3andak 3al telephone eno tsha8elak hay service te2dar ta3mel 3alyha acces hala2 law badak security tkoun 3alye bt7ot la2
so2al tene le byes2lak eno hal heye enabled   hal heye fa3ale tab3an fa bt7ot mark 3lyha w bt3mel finish fa byenshe2lak service
fa hala2 howe anshe2lak service 3ebra 3an class baset class myservice 3emel extedn mn class esmo service 2awl ma3lome badna na3refa 3an l class eno heye extend mn class esmo service
w fe constructor 3andak fade by diffult
w fe 3andak method esma OnBind hay l OnBind hay l method bas mnest5dema lal bound service fa 7alyan ma bade yeha mnl8eha l2no ne7na hala2 3am na3mel background service

hala2 service ela life cycle metl ma 3ana activity ela life cycle 2aw fragemnt kamen l service ela life cycle fa 5elna n7ot methods ta3wleft life cycle la nshof aymta bteshte8l aymta btetfe oncreate(),ondestroy() w fe method moheme esma onStartCommand()
hala2 l oncreate ,ondestroy ne7na fahmaneno eno wa7de 7a yen3mla call 2awl ma tenshe2 l service w tenye 7a yen3mla call bas tetdamar l service fa mna3mel log cat 3lyhoun wa7de oncreate wa7de on destroy la nshof keef bteshte8l

tyeb sho heye l method le esma OnStartCommand():
alak wa2t kont tet3emal ma3 l activity keen fe 3andak oncreate btem ested3a2a 3end enshe2 l activity w onstart() btem ested3e2a 3end tash8el l activity fa 2alak bel service nafs l she
alak service fe3lyan l onstartCommant() method btem ested3e2a 3end tash8el l service ya3ne l code le bade yeh yeshte8l 3end tash8el l service bdefo bel onstartCommand() method tab3an hay l method feha parameter feha intent feha flag inter law 7ebeb teb3ata w feha startid integer le howe l id le mn5elelo enta momken twa2ef service le heye sha8ale w hay l method bt3mel return la 2eme integer
ya3ne return value howe integer l integer heed  bekoun wa7de mn haw l 3 sha8let:
START_STICKY:Law 2eltelo return start_sticky fa ma3neto enta 2awl she sticky ma3neto laz2a she mlaze2 fa return start_sticky ma3neto eno hay l method l onstartCommand() law system tafaha abl ma tkamel sho8la rja3 sha8ela la wa7dak ya system ya3ne hawde l 3 variable le m3rafen ma 2eloun louzom ela b7ale wa7de sho heye l 7ale hay law system 3emel kill lal service abl ma t5les sho8la  nefham aktar sho azde
ne7na 7kena eno 3ana background service fa ne7na mna3ref 2awlaweyta d3efe fa law enta msha8el feha hay l service music w mara2 3lyha fatra tawele w enta fat7t kteer applications bheda l wa2t fa system 7tej la ram zyede w 7ab system eno yel8e sha8le 2aw ysaker sha8le mn l mawjouden 2aw ya3mela kill b ma3na tene fa la7 yetar ya3mel kill la service tab3te l2no 2awlaweyata d3efe fa l cosntant le byerja3 mn l onstartcommand ma3neto eno sho system ya3mel law sar fe kill la service sho system ya3mel la ay service hal yerja3 ysha8ela mara tenye 2aw dal tafye 2aw teshte8l w traje3 l intent
fa start_sticky ma3neta law system 3emel kill la hay service abl ma t5les sho8la ya3ne abl ma tnafez l code le mawjoud  bel onstartcommand rou7 2olo la system yrou7 ysha8ele hay service mara tenye  3ashen tkamel sho8la hay service bas mn8er ma teb3tla l intent hay le mawjoude 3ande bel parameter ya3ne 5ale l intent l mawjoude bel parameter null ma teb3tla l intent
START_NOT_STICKY:ma3neta law system 3emel kill la hay service abl ma t5les sho8la ya3ne abl ma tnafez l code le mawjoud  bel onstartcommand ma tsha8ela mara tenye ya system ela iza fe pending intent mawjoude ya3ne law keen fe pending intent mawjoude rou7  sha8ela mara tenye bas law ma fe intent mo3ala2a ma tsha8ela mara tenye ya3ne law enta kenet mest5dem pending intent ma3 service rou7 sha8ela mara tenye
START_REDELIVER_INTENT:ma3neta law system 3emel kill la hay service abl ma t5les sho8la ya3ne abl ma tnafez l code le mawjoud  bel onstartcommand rou7 ya system sha8ela mara tenye w rselaha l intent hay le mawjoude bel parameter 3asehn te5od mena l data le bada yeha
fa hawde l 3 constant lezmen bas fe 7alet eno system ra7 fasal l service abl ma t5les sho8la


hala2 ne7na bel background service lezmna bas function l onstartCommand:
fa hala2 bade 2a3mel application baset application besha8el music ka service
fa hala2 7a jeeb file music m3yan 7oto b file esmo raw
fa hala2 bel class l myservice bade sha8el file l music fe 3ande class esmo MediaPalayer heda class la tash8el l music l sounds
fa 2awl she ba3mel obejct meno declaration global w bel oncreate bjahez heda l class ya3ne abl ma tblesh l service bjahez l sound l onreate 2awl ma  tenshe2 l service rou7 jahez l media player w heda file l sound ta3ele l music
w bel onstart bade sha8lo la heda l sound
fa bel oncreate b2olo medaipaler equll MedaiPlayer.create() w ba3te file l music ta3ele w ba3te kamen l context
heek ana bkoun ansha2t l media player w jahzto
tyb keef bade sha8el bel onstartCommand() method b2olo mediaplayer.start() ba3mel ablo shart baset eno iza keen l media player dot is playing mano sha8al !ispalaying rou7 sha8lo
3ashn law l most5dem sha8al servies wara ba3dha 3ashn ma yrou7 ysha8el aktr mn sout ma3 ba3do  w yedmejoun ma3 ba3d w 3mele return start_Sticky
bel ondestroy brou7 bef7as iza keen l mediaplayer dot isplaying 3mlo stop mediapalyer dot stop
w ba3den media player dot release
hala2 ana heek 3mlt service 5alsta bas ma sha8lta la sha8ela ana 3mlt 2 button wa7de start wa7de stop
w ba3mel onclicklistner 3ala tnaynetoun

hala2 keef bade sha8el service hayen keef kent tsha8el l activity nafs she:
bt3mel Intent  :Intent intent=new Intent() w ba3te l context tab3 l activity w ba3te l service le 3mlta ana
bas badel ma t2olo startactivity hala2 bt2olo startService() w bt3te l intent
haw bel nesbe lal start button


ama bel nesbe lal stop button kamen ba3mel intent nafsa zeta bas b2olo Stopservice w b3te l intent


note:aye 3onsour mn  l 4 commponent ta3wlet l android lezm ykoun m3araf bel maninifct fa iza fotna 3al manifist 7a nshof eno m3araf 3ande service

hala2 bas 3mlna run w kabsana 3ala button l start l oncreeate n3mala call w ba3den l start w shata8let l 8oneye w ba3den iza jarbna netla3 mn l application bel home screen 7adala sha8ale l music l2no heye sha8ale bel background w ba3den bas kabsana stop n3mal call lal destroy method wa2af l music w kamen jarbna iza kenet sha8ale l music w tl3na kolayn mn l appp ya3ne 3mlna navigation w shelna kel l application mena la7 tetfe ama iza tl3na mn 5elel l home screen 2aw back mala7 tetfe l application
hay l service le sha8lenha kola hay 3ebra 3an background service service bteshte8l bshakl kemel bel background w t2akda eno background l2no iza sha8leha w tl3na mn application 3am dala sha8le l 8oneye w 5ale belak ma fe aye tawasoul baynak w been l music l sha8ale ya3ne ma fe notification eno twa2efa 2aw she lezm la twa2efa tetdo5l 3al app w twa2efa


metl ma 2olna ma fe 2aye tawasoul been service nafsa w been l activity fa fe 7al keen l code sha8al bshakl kemel bel background mndoun aye tawasoul been l user w been service nafsa btetsama background service
heye tameman sha8ale bel background w 2awlaweyata d3efe yemken law system 7tej aye memory yrou7 ya3mel kill ela w yel8eha



hala2 bade wade7 l far2 been l service l 3adeye w l Intent Service mn 5elel esmple tene 8eer l music bade 2a3ml service w b2lab l startcommand bade 7ot code ye5od wa2t kteer 3ashen wade7lk sho l mashekl l mawjoude b class l Service
iza mnetzakar wa2t sha7na l thread  2olna fe she 3ana esmo thread.sleep() btnayem l thread bt3atel l thread la mode mo3yane msln 5 sawene
fa bade 2a3ml code bel onstartcommand eno 3atel l thread 5 sawene lesh 3mlt heda l code ana 3mlt heda l code simulation mo7aket la aye code bye5od 5 sawene
fa ana 3ashn jareb shof keef class l service byetfe3al ma3 code bye5od wa2t 5 sawene  3mlt heda l code
enta badel heda l code feek t7ot aye code bye5od wa2t tawel 3ade
hala2 5alene jareb 2a3ml run w shof sho bado yseer

3mlt start bade ed8at mara tenye start batal yed8at lesh da8at reje3 ba3d 5 sawene lesh ba3den 3ala2 l application ma3e 3am 7ewel ed8at start w stop 3ala2 l application ma3e lesh ?

-2awl sha8le badna nefhama b class l service eno l code le mnektbo bel onstartcommand byeshte8l bel userinterface thread 2aw bel main thread tyeb sho 2a3mel 3ashn 7el l meshkle hay 2alak momken ta3mel thread monfesle  2aw ta3mel handler 2aw async task,excutor  2aw le badak yeh fa 2awl ma3lome badna nefhama b class service eno l code bekoun mawjoud byetnafaz bel main thread heda 2awl 5ata2 fene 7ola 2a3mel thread monfesle
-hala2 iza sha8lt l application w bas ekbous start mafroud heda l code le mawjoud 3ande bye7jez 5 sanwene w ba3d l 5 sawene bet5les mafroud eno service bt5les sho8la ba3d 5 sawene  kamen b exmple l music abl shway mafroud bas te5las l music tetfe service la7ala bas houn ma sar heek eza kabsana 3ala l start b exmple l service 2 3mele create mara2 5 sawene w ma tafet serivice dalet sha8ale  ma 3mlet destroy w nafs she bel nesme lal music 5elset l music w dalet sha8le service fa tene meshkle b class service  eno ba3d ma tentehe service 3amala ma btetfe la7ala lezm enta yadweyan tetfeha ta3mel stop kabset l stop la tesd3e l ondestroy ya bt2olo stop service 2aw mn class service t2olo stopSelf() momken testd3eha mn de5el l service nafsa 3ashn tetfe bel service  fa tene meshkle mawjoude b class service eno ma btetfe la7ala  ba3d entehe2 l 3mlye lezm enta tetfeha yadaweyan ema mn bara best5dem l stop service 2aw mn jowa stopself() metl l activity wa2t ta3mel finish bas keef momken bshakl da2e2 b service ta3et l music 2a3ml stop self momken 2a3mel listner b2lab l media player fe listner fe listner esmo mediaplayer dot setonstopcomplistionlistner ya3ne lama yseer complition ektemel lal music rou7 3meel stopself() fa heek fe3lyan 7a ta3mel stop la service 2awl ma te5las l music fa le sar hala2 2awl ma 5elset l music 3mele stad3a l ondestroy
-hala2 telte meshkle la tbyen ma3e rje3t la service l music 7atet zyede 3ala l onstart logcat onstart teb3a mojarad ya3ne betbyen bas yen3amal start la service eno start service fa le sar eno kel ma ed8at start berou7 byestd3e service created ,service start ya3ne iza kabsta l start button 5 marat ya yesd3ele 5 marat wara ba3d oncreate(),onstart() le sar eno ma natrne 3ashen tenthe l service l 2ola mn tanfez l code l 2awl ra7 sha8al l start ma3 ba3da fa ana law ma 3emel check eno if !medaiplayer.isplaying() mediapaleyer.start() law ma 3mele heek keen sha8le aktr mn music ma3 ba3d ya3ne howe ma byontor 2awl service te5las la tblesh teneye ya3ne moken ysha8el tnene ma3 ba3d 3ade fa l meshkle 3 eno ma btem jadwalet l 3amlyet b queue ya3ne kel ma sha8el service betrou7 bteshte8l 3ade ma bye5od b3een l 2e3tebar wala fe service sha8ale hay fa ana ma 7a sha8el hay la t5les l 2ola la2 berou7 besh8el dene kola ma3 ba3d ya3ne howe lezm kel mara tetd8t eno sha8el service trou7 hay l 3mlye teshte8l nerja3 bas nekbes mara tenye tfout l 3amleye b2lab quue ma betle3a la teshte8l ela la tetla3 l 2ola mn queue w besh8eloun eno first in first out 2awl wa7de kabsta heye 2awl wa7de btetnafaz bas t5les tetla3 mn quque bteshte8l teneye  bas kamen ro7na ne7na 7lyneha yadaweyan best3emel check eno iza keen fe music sha8le wala la2 iza sha8le ma tsha8ale sha8el iza fe she sha8al ma tsha8el she


fa mnwara haw l 3 mashekel l kbaar 3mlolna  class tene esmo Intent Service bt7elel haw l 3 mashekel sa7 ana fene 7el haw l 3 mashekl yadaweyan ya3ne meshklet l main thread fene 2a3mela b thread la7ala meshlet eno ma bto2af la7ala momken 2a3mel stopself ,meshklet eno btsha8el services tenye bala ma t5les l 2ola fene 7ela mn 5elel l check if else bas houne raya7on w 3mlolna class jehez btry7ne mn kel hay l mashekl:

fa badna nshof keef na3mel class l intent serves mna3mel new service badel ma ekbous service mnkbous IntentService
fa bya3mele methods fe b2laba methods kteer jehze fa 5alene sheel kel hay l methods hala2 la nshof keef fene tabe2a mn scratch

fa hala2 class l IntentService:
l Intent Service howe class byeshbah class service bas 7alo fe l mashekl l  le mawjoude b class service
3ande 2awl she constuctor bas fe b2labo super() byeb3at fe esm l class lal IntentService le howe l class l 3emlen ne7na extend meno lezm ykoun mawjoud super w b2labo esm l class ta3ele la eb3ato lal ab

w fe method esma OnHandleIntent() batal fe method esma onstartCommand() sar fe method badela esma onhandleIntent bte5od parameter intent
hala2 3mlt oncreate ondestroy nafs le 3mlto bel service l 3ade

hala2 bade 7ot l code taba3 l thread sleep bel onhandleintent nafso zeto w bade shof sho bseer:
w 5alena nshof heda l clas sho bya3mel?:
-le sar 3mlt run kabst start n3ml call lal service created,service started ba3den ba3d 5 sawene la7alo 3mel service destroyed fa hay 2awl meshkle l IntentService 7alale yeha eno howe la7alo byetfe service bas te5las ma bade 2a3ml stopself
-da8t start rje3t da8at start wara de8re nda8at 3ade ma 3la2et l button fa heda dalel l code le mawjoud 3ande byeshte8l b thread monfesle bel worker thread ma byesht8el bel main thread w hyde tene meshkle 7alale yeha eno l code le 3ande byeshte8l bel worker thread fa mane b7aje ba3d 2a3ml thread monfesle w hal a5bar kolabel serice 7alale yeha l intent service
-da8t 3al button 3melet kabseten wara ba3d 3emel  create,start rje3t ba3d 5 sawene 3mele start ma sha8le l service kolounma3 ba3d la2 jadwale l she8l kelo b queue w 7a toun 2awl ma t5les 2awl service 2awl kabset button ya3ne ba3d 5 sawene sha8le teneye heek ma sha8le l kel service ma3 ba3d la2 natar w ba3d ma y5les l da8ten  2aw kel da8tat le kasboun bya3mel la7alo ondestroy fa hay telet meshkle 7alale yeha eno bjadwel she8l b queue

fa l intent service:
-btem tanfez l code bde5el worker thread
-btem entehe2 l service la7ala ba3d entehe2 kel l 3amlyet
-btem jadwalet l 3amleyet kola b queue

note:law enta jarbt tsha8el file l music b2lab l intentservice mesh 7a yeshte8l lesh?
l2no l music fe3layn bte5od jez2 kbeer mn ram fa yofadal tet3mela ma3a b class l service l 3ade w t7el l mashekl le fe bshakl yadawe

w heda kel l far2 been class l service w class l intentservice


ForegroundService:
fekret l foregroundService eno kel ma zeed tarabout l user interface ma3 l 3onsour l service kel ma zedet 2awlaweyt baka2o b system ya3ne 2awlaweyto btseer 2a3la keef ya3ne?
ya3ne ana 3ande l background service sha8ale bas ma fe 3ande wala 3onsour wala aye tawasoul been hay service w been l user fa ne7na bel exmple le abl shway lama sha8lna l music dalet sha8ale bel baackground ma kena 2adren abdan  netfeha ela iza rj3na 3ala l application w na3mel stop service fa ma keen wa2t kenet l service sha8ale bel background aye tawasoul bayne ka user  w ben service ya3ne heye w sha8lae bel background w ana barat l app ma fene etfeha wala sha8ela wala she ma fe tawasoul bayne w bayna fa 3ashn heek heye tamemna sha8le bel background
fa 3ashn heek kel ma keen fe tawasoul ya3ne law keen fe rabet mo3yan bayne w been service kel ma zedet 2awlaweyt baka2a
fa 3ashn heek hala2 la7 nshof l foregreound service le heye fekreta ya3melak service teshte8l bel background tameman metl l backgroundservice laken yorbota ma3 aye she l most5dem yetfe3al ma3a  aye she bel user interface w lheye l notification
fekret l foreground service  nafs fekret l background service bedoun aye ta8yer laken btem rabta b notification w l notification hay btkoun sebte ma btrou7 ela lama tentehe l  service lesh 3asehn 2awlaeeyt bake2a bel nesbe lal system tkoun 2a3la 3ashn lama system yeje la yshela yla2eha mortebta b user interface view 2aw element fa y5leha ma yshela fa 3asehn heek 3mlolna l foreground service w rbatoha ma3 l notification btertebet ma3 notification 3ashn dal 2awlaweyta 2a3la mn l background service 3asehn heek mnshof kel application l music 2aw l bta3mel meno download 5elel tash8el l music btl2e fe notification mawjoude law 7ewalat tel8eha 2aw te7zefa ma btou7 lesh l2no fe3lyan heye foreground service  ma btou7 ela lama tentehe l 3amlyel2no law ra7et btseer bakcground service ya3ne ma feha trou7 heek
keef badna nest5dem background service?
nafs le sht8lne bel zabt bas le bade mayzo 3an bakground service bade 2orbota b notification keef bade 2orbota b notification
nafs l code taba3 notification le 3mlto ana abl 3mlo b method w 7ota b class l service
le bade 2a3mlo hala2 bade erbout service b notification  bas ma bade sha8ela lal notification ya3ne shel l notify l push
bade 2olo retun b2lb method l notification
return  bulder.buld; ya3ne raj3le l notifcation w bade 2olo eno return type heye notification fa 5ale esm l method getnotification
enta mokken ta3mel aye sha8le 8eer notification aye sha8le w terbota b service 3ade

fa 3ashn hala2 5aleha foreground service ba3mel sha8le wa7de
jowa l onstartcommand() method
b2olo startForeground() bye5od id w notification fa ba3te aye id le howe nafs id notification
w ba3te l notification le howe getnotification
fa startforeground ka2no b2olo 2orbot l notifiction le 3ande bel serivice hay fa hala2 2awl ma teshte8l service 7a ytale3 l notifiction w 2awl ma tetfe l notification 7a yshel l notification
w badel ma 2ol startserviece b2olo startforegroundservice hala2 bel version l 2ademe keno y7oto   ContextCompat.startForegroundService(getApplicationContext(),intent);
w 2e5er sha8le badna ne7ke 3ana heye l intent le mawjoude bel onstartcommand hayde l intent keef btenba3at ne7na bel notitification ma kona neb3at ontent w na3mel button fa hyde l bas nekbesa btenba3at l intent la heda l class
fa when best2bela best2bel bel onstartcommand mn 5elel hay l intent l mawjoude bel parameter hay be5sous notification w kamen nafs she b5sous l bound enta bteb3at intent mn 5elel activity

BoundIntent:

hala2 badna ne7ke 3an l bound service:
heye 3ebra 3an service 2aw 5edme mortabeta b activity malsn ya3ne bekoun fe activity sha8ale w l activity hay mortabet feha service kel ma l activity sha8ale l service sha8ale  law l activity sakaret damaret l service la7ala b tsaker btetdamar
fa ne7na lezm nerbet l activity bel services mn 5elel methods m3yane
w lezm nefham eno ne7na lesh mn3oz l bound service l bound service m2same naw3en:
local w remote
ne7na mna3ref eno 3ana 4 commponent bel android bekono bel processor metl serivice w activity broadcast,content provider w kel application ela nafs she

fa hala2 ne7na mne7ke eno badna nerbout l service bel activity fe 3ana 2 type:
type 1:local
local heye bas terbout l activity w service b nafs l activity ya3ne ne7na 3ana activity w service bel application bas nerbouton ma3 ba3d hawde le mawjoude bel application nafso mnkoun 3am ne7ke local
remote:heye bas nerbout service w activity ykono mn different application ya3ne 3ande application esma A feha service w feha activity w 3ana application B feha service w activity fa hala2 bas norbout l activity le mawjoude b application A ma3 l service le mawjoude b application B mnkoun 3am ne7ke 3an remote
local:mnest3mel she la na3mela immplement she esmo IBinder howe 3ebra 3an interface
remote:mnest3mel she la na3mel immplement she esmo Messenger

hala2 le bmayez l bound service 3an l start service le houne l background w foreground
eno l bound service fena ne5od mena data fena na3mel ma3a interaction ya3ne t5yal 3andak service betla3e random number mn 1 la 100 tyb bade hayda l number eb3ato lal activity w 7eto b textview fa hay ma btemshe ela bel bound service ne7na mneb3at la service eno badna hay data w service berj3le yeh w b7eto b textview
akbar exmple 3an l bound service howe server nafs she bya3mel request w l server berj3lk data w enta bta3mel handle la hay data


ne7na hala2 badna nblesh bel local:
hala2 t5ayal 3andak service m3yane w Activity m3ayane

service le 3ande yeha bta3mel generated la random number ok
l activity b2laba textview le bada ta3mel dispaly la heda number le bta3mlo generated service

le bseer heda senario

l activity la te2dar tjeeb heda number lezm ykoun fe connection bayna w been l service fa heda l connection mn2olo Bind service eno l activity bada ta3mel la service bind ta3mel connection bayneta
ba3d ma ta3mel connection w l connection ykoun succefully sar feha l activity teb3at la ssystem eno ya system 2olo la service eno bade random number fa berou7 system be2olo la service fa tou7 service bteb3at l random number lal activity fa l activity btest2bela w bt7ota b textview
ya3ne iza kenet l activity 3emele bind lal service bshakl sa7e7 7a te2dar teb3t l random number la hyde l activity
iza mana ma3mola bind mazbout mesh 7a te2dar lal activity teb3at eno bada random number wala l service teb3at l randm number ma 7a ykoun fe tawasoul ya3ne

Service <-----Bind Service----------- Activity
        <------Ask for random number-
        ------return exmple number 55---->



hala2 ne7na 2olna 2awl she lezm na3mel bind lal service l activity ta3mel bind lal service heda she bseer mn 5elel method mawjoude bel service btre3 object esmo IBinder

hala2 la yseer tawasoul been l service w l activity badna nest3mel api esmo Service Connection la na3mel connection been l activity w service


hala2 badna nblesh na3mel immplement lal service class 2awl she

2awl she badna na3mel integer esmo mRandomNumber declaration heda l variable 7a ya3mel hold lal random number le service 7a ta3melo generated
ba3den badna na3mel boolean varibale esmo mIsRandomGeneraatedOn heda la7 nest3mlo la nwa2ef l generated random number 2aw na3mel on eno 3meel generated lal random number
w ba3dem b3ref 2 firable final wa7ad equll 0 wa7ad equll 1000 l2no ana bade l random number been 0 w l 1000

hala2 la 2a3mel generated la random number 3ande 2 methods wa7de esma startRandom number hayde bt3mele generate la random number w method tenye esma getRandomnumber bta3mele return la random number
bel startRandomGenerator:bade 2olo eno iza keen heda l boolean misrandomgenerated ==true kel ma ken heda l boolean true bade 2a3ml loop w ana ma bade number yetla3 bsor3a bade l 3amlye te5od wa2t fa b2olo nayem l thread seneye wa7de w 3mel random number w sayvo b heda l vairable le esmo mRandomnumber
w bade 2a3mel kamen method esma StopRandomNumberGenerated w 7ot feha eno heda l boolean equll false

hala2 ne7na mna3ref eno service bas tblesh btestd3a method l onstartCommand b2laba bade 7ot eno 5ale heda l boolean generator true w bade 2a3mel worker thread 3and b2lba bade yen3amal random generator 3ashn ma 3atel l yu l2no ne7na b2lab method l startgenerator 7atyna thread.sleep

hala2 bel destroy badna nwa2ef l random number ya3ne bas l service badna n2olo eno wa2ef generated la random number fa b2lba mna3mel call la method l esma stopgeneratedrandomnumber


hala2 mnje lal main part badna nest3mel method le esma onbind hayde l method le bada terbout l activity bel service fa hay l activity btrje3 object mn no3 IBinder
fa badna na3mel clas esmo MyServiceBinder immplement inerface esmo Ibidner 2aw extend binder l2no kamen l binder howe extend ibinder nafs she
b2lab heda l class mna3mel method esma getservice hay l ethod btrjae3 object mno3 l clas le 3mlo ana le mn5elo ana bade 2osal la heda l class hyda l method bas betraj3le object mn heda l class

hala2 mna3mel obejct mno3 Ibinder binder w mn5le equll la heda l class lesh l2no be method l esma onbind bade raje3 lal activity l hyde l object w hyda l object sho 7aykoun b2labo 7aykoun b2labo method le btrj3le l service le mn5elela 7a 2e2dar 2a3mel genrated la random number     private IBinder mBinder=new MyServiceBinder();

hala2 mnje lal main activity ne7na badna na3mel 4 button w textview wade start service wa7de stop service wa7de send service wa7de bound service wa7de unbound service

hala2 bel main activity ba3d ma 3mlt l ui w inflate lal utton w textview
bade 2a3mel 2awl she instance mn class ta3ele
ba3den bade 2a3mel boolean isServiceBound
w 2olna la yseer fe tawasoul been l activity w l service lezm nest3mel api esmo ServiceConnection ba3mel kamen meno instance


hyde l interface le esmo ServiceConnection b2labo 2 method call back byen3amlou call la7loun
2awl method esma ServiceConnected iza n3mala la service connected ma3 hay l activity succefully fa byen3ama call w tene method esma on service disconnected hay byen3ama method iza l activity ma 3mlet connet ma3 service

method l onserviceconnected bteb3at parameter service Ibinder bteb3at l object mn ibidner le howe l object le mn l class le 3mlto fa ana ba3mel variable w best2blo ya3ne iza neje7 l connection jeeb heda l class 7afzo b variable rja3 b2lab heda l class 2olna fe method le btjeble service fa bjeeb service w ba3d ma jeeb service ta3ete b2olo la variable le 3arfto fo2 l boolean eno l binder sucefully
w bel disconnected b2olo la heda l varibale l boolean eno l activity ma n3mla bid a3 l service fa false


le sar step by step:
2awl she bas kabsna start button 3ade service shata8let bel background ma heye l bound service feha tkoun startservice bas l start service ma feha tkoun bound service ok
w bas kabsna stop service byen3ama stop la service 3ande kamen

hala2 b kbset l onbind:hayde l 3mlna intent mn heda l activity la bound service w sta3mlna method esma bindservice hay l method bta3mel bind lal activity ma3 service ya3ne bteb3at request lal andoid system bt2olo 2olo la service bade 2a3mel bind ma3o fa hayde l method bte5od intent w bte5od connection b2lab hay l connection fe 2 method 2awl wa7de eno if connection succefuly hayde aymta byen3mal call ne7na ma ba3tan la system t2ol l service eno bade 2a3mel bind iza l service 3mlet bind m3 l service fa byen3amal call la method esma onsuceefulyconnnect ya3ne sar fe connect ma3 l service ya3ne l bind succefuly w bhay l method byeb3tle esm l service le 3mlna ma3a bound w byeb3at l bound service le heye object mn l Ibinder mn 5elel l method le mawjoude 3ande bel servie ta3et l on bind ma heye btrje3le l bind object eh fa ana best2bela  hayde mnest2bela b variable w mna3mela cast mnafs no3 taba3 l inner class le 3mlto le howe l binder w ba3den bjeeb meno l service tab3te mn 5elel method maawjoude bel b2lb heda l class le heye getservice w ba3den b2olo eno l servie n3amala bound
hala2 law service ba3tetle eno heye ma feha ta3mel bind masho8ole kenet n3amal call la tene method on fail connection fa b2lab hay b7ot eno b2lab l boolean variable eno not bound

hala2 iza ne7na sha8lna l service w ma 3mlna bind fa heye 7a ta3mel call lal oncreate start w iza 3mlna stop 7a ta3mel call lal destory
bas iza ma 3mlna bind bas connect w jena la na3mel display la number generted ma 7a ne2dar l2no heye bhay l 7ale btkoun start service background service fa ma bekoun fe intearction bayna w been aye commponent
ama iza kabsna kabset l bound fa houn sar fe conntion bayna w been l activity fa mne2dar nosal l generated number

w 3mlt ana button emsa unbind hayde l buuton st3mlt method esma unbind bte5od connction fa hay bte2ta3 l connection bayna w been l service w bta3mel call la method mawjoude bel life cycle ta3et service esma unbind


w iza jarbna na3mel bind la service w heye manasha8le ma mne2dar kamen l2no keef badak ta3mel connection ma3 she lisa mesh mawjoud ma feek
w iza kena 3mlen bind la service w jena la nwa2ef servie na3mela stop ma mne2dar byemna3ak system l2no be2olak hayde l service ma feha tetwa2f l2no fe inteacrion ma3 l user
w iza jena 3mlna un bind la service rj3na jena la na3mel bind service l onbind method ma 7ayen3amal call lesh l2no heye byen3amala call bas 2awla mara w btenma3ak la connction method fa mhma saret hounek betbtel yen3amala call w bel nesbe lal unbind kamen nafs she hay l method ma byen3mala call ela 2awl mara
ya3ne iza 3mlna kabsna 3ala l bind button fa heye 7a yen3mala call la bound method rja3 kbous bound mara tenye ma 7a yen3mala call ad ma kabsta l2no heye bas 2awl mara byen3mala call w btenbe3et lal  connection
w kamen bel nsebe lal unbind method iza kabsna unbound button 7a yen3mala call 2awl mara bas tene mara bas teje la tekbesa ma yen3mal call lal unbind method
w iza kabsna stop service w kenet w 3mlna bind olna mesh 7a yen3mala stop la service bas iza kabsna unbind la7ala 7a terja3 ta3mel call lal stop service
bas iza kabsna la7la bound service w service mana sha8le byen3mal call kel mara lal onbind w kamen bel nesbe lal unbind


ad ma ken 3ana 3ada activity feha ta3mel bound la nafs service
l bound service ma byen3mala destory la7la ela iza ne7nawa2fneha w ma bseer feha expetion ,resource crush w heda she depend 3al exeption le mneb3to bel bindservice hayne b 7alet l Context.BIND_AUTO_CREATE 7a yen3mal create lal service la7ala w 7a yen3mal call lal bindservice method la7ala bas ma 7a ta3mel startcommand ma 7a tblesh l service bas bye3mla create


w 2e5er note lal bind serverse l commponent le bte2dar ta3mel bind ma3 service heye service,activity,content provider


hala2 badna nblesh bel remote type remote heye btkoun been 2 application badna na3mel bind la service mawjoude b Application A ma3 Activity mawjoude mawjoude b application B

hala2 senario nafso 3ande :
service mawjoude b application A
Activity mawjoude b Application B

Service in application A <-----Bind Service----------- Activity in application B
                          <------Ask for random number-
                          ------return exmple number 55---->


note:ne7na badna na3ref eno fe 3ande she esmo queue message b2lab l process taba3 l application hay l qeue bte5od task sho houne task task 5asa bel ui metl update button textview kaza fa ne7na bas nkoun b worker thread w badna neb3at task mn task la heda l queue w hyde le queue meen be nafez l taks fe 7awlyha constant loop heda 5aso bel main thread heda sho8lto ynafez haw task  fa bas ykoun 3ande worker thread w bade eb3at task 3ala heda l queue ma fene mobshartan fe 3ande class esmo handler fene eb3tlo task hyde w heda l handler 3ando she esmo runnable fa berou7 l handler byeb3at task la hyde l queue w berkaeb 3lyha handler sa3eta lloper benfez task heek btsser l 3amlye

fa hala2 ana 3ande 2 process ne7na mna3ref kel app 3ando process ana bade l service le b process A ta3mel commuucation ma3 process tenye b application B

service would still implement bel on bind method  ba3dna nafs  l onbind method 7a ta3mel return lal binder ya3ne 7adal nafs she bas la7 yseer ta8yer sho howe ta8yer:

service hal mara 7a test5dem she esmo Messenger w handler
iza fotna 2rena 3an l messenger sho howe l messenger w l handler api:
la7 nshof eno l messenger howe 3ebra 3an binder fa 7adal bhay tare2a 7a dal 2e2dar b method l onbinder w 2a3mel return la binder mn 5elel heda l messenger le heye meseenger.onbind()
w kamen 3am y2olona eno heda l messenger 3ando referance 3ala class l handler le be2ololak eno mn 5elelo fe 7ada yest5dmo w yeb3at messeage 3lee fa bhay l 7ale iza keen l thread yeb3at task 2aw messeage  3ala l messeage queue fa bye2dar mn 5elel l handler le mawjoud
w l messenger ta3elna  3ando referance 3ala l handler  w bas 3am n2ol eno momken 7ada yeb3at messeage 3lee la heda l handler bhay l case aye 7ada azdoun another app application tenye bte2dar teb3at messeage 3ala heda l handler fa le 7a nshofo eno another process 2aw another application bte7tewe 3ala activity 7a te2dar teb3at messeage lal messenager w be ma2no l messenager 3ando referance 3ala l handler w b2este5dem l handler la7 ne2dar na3mel handle la hay l mesaage le wesltna mn application tenye

hala2 la7 nshofo bel immplimination:
2awl she 3ana application A fe b2laba activity 2 button w service hy l applicatio btsha8el service w betfe service bas heek mn 5elel l buttons le 3ande ya3ne 3ande application seperated besha8el service
hala2 la7 na3mel clas service fe b2labo nafs l method ta3et generated number nafs kel l medthod bas le 7a yet8ayar houn l onbind method

-2awl 5etwe badna na3mela eno na3mel variable esmo getcount=0 heda l variable la7 est5dmo la heda l variable ka flag iza tla2et request b2lbo heda l variable getcount heda ya3ne eno fe sha5s m3ayan 2aw application m3yan byotlob mene ersel ra2m random la 2elo
-tene 5etwe bade 2a3mel class esmo RamdomNumberReuestHandler heda l class bade 5ale ya3mel extend handler w b2alb heda l class bade 2a3mel overide la method mawjoude bel handler esma handlerMessage w hay l method take parameter object mn Message b2lab hayde l function bado yoslne message mn bara l application btsolne men l parameter le b2lab hayde l method fa bade 2a3mel check iza hayde l message le wsletne fe b2laba variable esmo what iza keen heda l what equll la variable le 3mlto le esmo getcount  ya3ne ana 7a ysoalne message mn bara l application bade 2e5od mena l variable les esmo what w 2a3mel check iza howe equlll la heda l variable le esmo getcount iza keen equll metl ma 2olna ma3neto eno fe 7ada 3am yotlob mn server random number
- fa iza keen l what equll l getcount la7 2amel objct mn message w 7ot feha heda l getcount variable hala2 iza 3mlna object mn mesage Message message=new Message() hay tare2a bte5od kteer mn ram fa fe 3ande b2lab class l message message jehzen mn 5elel method esma Message.obtain() hayde btredle message jehze w bte5od 2 parameter 2awl wa7ad handler tene wa7ad integer fa ana 2awl parameter b7opt fe null tene parameter b7ot fe l variable le esmo getcount fa houn ka2no 3am 2olo 3atene object mn no3 message w 7ot fe variable esmo getcount heye hay l method bet5od handler w tene paramter howe sho 2emet l waht la hyde l message fa ba3te l getcount
-ba3d ma 3mlt l message bade 7ot b2labo random number b2lab variable esmo arg1 ma ne7na sho hadfna howe 3emel request eno bado random number w ba3tle l l variable l what fe b2albo getcount fa ana jebt meesage 7atet feha heda l variable w ba3den bade 7ot b2lab hayde l message l generated number b2lab l variable le bel message le esm arg1
-ba3d heek ma 3mlt l message w sar fe b2laba l random number bade erja3 eb3tlo yeha lal application tene keef ana ma3nde l message le b3tle yeha bade jeeb 3enwena keef mn 5elel method esma replyto() hayde btjeble 3enwen hay l message ba3den m2olo replyto.send() ya3ne b2olo b3tle 3ala hayda l 3enwen l message le 3mlta le feha random number w hayde bten7at b try w catch heye bel 7a2e2a msg.replyto btjeble l messanger taba3 hayde l message le mab3otetle w 3ala hayda l messanger bade eb3at hayde l message hala2 bhay tare2a 5lst immolent la heda l class


hala2 b3d ma 5alst l class l handler bade 2a3mel object mno3 messanger w l messanger 3ndo referance 3ala handler fa bye5od handler ya3ne ba3mel object mno3 messanger w beb3tlo b2lab l parameter object mn l class le 3mlto messanger 3ando referance 3ala l handler fa iza tam ersel resle la heda l messanger l handler will handler this message iza l message nba3tet la heda l messnager fa 7a nestel3m hayde l messnge mn 5elel l class l handler le 3mlto 3ashn heek howe bye5od handler 3ashn yestelem l messages le btosalo la heda l messanger


hala2 e5er she b method l onbind abl shway ne7na 3mlna class mn l bind la7ata nraje3 ibinder bas bhay l casse l messnager howe 3eabra 3an binder fa ma mne7tej na3mel clas mna3mle bas heda l object le 3mlto dot getbinder()

w bhay tare2a 5alsna class service kolo

hala2 bel main activity bade 2a3mel kabse btsha8el service w kabse btfe service

fa hala2 sar 3ande 2 button 2awl button bas ekbousa btsha8ele serves tene button bas ekbosa btafele servies


hala2 bade 2a3mel another application 3ando activity le 7a ya3mel connect ma3 hay servies heda le 3am ne7ke 3ano concept l remote bound services

hala2 badna nblesh b application tene bade same serversilde w la7 nshof eno keef l application tene le b2alba l activity keef 7a ta3mel connect ma3 l hay l serves w tjeeb random number meno w display it in activity

la7ed hala2 ne7na shta8lna l part l 2awl mn l logic le howe la7ed hala2 3mlna bel application l 2awl messanger w handler aye message la7 tosal  la heda l application 2aw serves lezm tomro2 bel messnager le 3mlne l messanger la7 yroeu7 yeb3at hay l message lal handler lal class le 3mlne w hayde l handler la7 testelm hayde l message  w ta3mel handle la messagele wesletla le howe eno tshof iza keen l what variable equll lal getcount ...ect

fa hala2 bade 2a3ml application jdeed  3ando activity  la7 na3mel logic code to coonect to servies le heye generated random number fa hala2 la7 nblesh
-fa 2awl she la7 nblesh na3mel messanger b2alb heda l application l jdeed la7 yetm tahye2t program l moresale be2ste5dem l Ibinder metl ma mnetzakar eno bel application l 2awl l b2labo servies  3mlna immplenet lal onbind method le 7a ta3mel return lal ibinder from messanger object that contain the handler fa heda l ibinder hala2 le 7a n2om b 2enshe2o bel activity  fa heda 7a ykoun 3ando referance 3ala l handler le 3mlnelo create bel application le b2lbo service la7 n2om bta3yen 2 addres lal message bhay tare2a la7 yet3araf l application le bye7tej la etesal bel service la7 yare3 l maken le bye7tej l etsel fe. la7 ytem etesal connect l activity la7 ta3mel connect bhay l service w la7 ytem tawjeh hay l message l mo3yane le b2lab hay l activity mn l ibinder lal messnager le b2lab l activity bhay tare2a la nkamel nous l circut

hala2 ne7na sta3mlna class service l 3adde w 7alyna kel l mashekl le fe metl eno byeshte8l bel main thread fa ro7t 3mlt thread monfasel hala2 iza badna fena nest3eml Intentservices l immplementation bedal nafs she bas le btefere2 eno badel ma 7ot l code logic bel onstartcommand bseer b7oto b method esma onhandle intent ya3ne badel ma yen3amal call lal onstartcommand bas teshte8l l intent servies bseer byen3amal call lal handleintent w bteb3tle l intent bas tektob l code b2lab l handle intent l code le b2albo byetnafaz tek2yan bel worker thread fa sa3eta mnshel l l newthread le 3mlne bel generatorrandom function
bel nesebe lal bound service nafs l immplement kamen w nafs she bteshte8l ma btefere2 l immplementation abadan ela eno 3ande method esma handleintent bas local wala remote nafs she

hala2 3ane meshkle ana eno iza keen 3ande service sha8ale 3ala app w tele3t mn app bel home button ba3d fatra she 2 min hayde l service la7 yen3ama kill la7ala w nafs she iza tel3t mn app kolyan la7 ta3emel kill lawa7da fa ne7na be7aje n7el heda she l2no mn l android orio w trl3 saret l service heek ta3mel
heda she badna n7olo be most3det class esmo jobintentservice n7ena badna na3mel class estend heda l class w na3mel overide la ba3d l method le fe le 5asa bel service bheda l clss 3ande methods nafsa zeta le mawjoude el service w l mawjoude bel intentservice bel 2edafe la ba3d l methods l 5asa bheda l class
hala2 so2al le bye5tour 3a balena keef fena na3mel start lal jobintent service?
3ande method b2lab heeda l class esma enqueueWork(contxt,class cls,jobid,intent work) hyade 3ebara 3an static method
2awl parameter bte5do howe l context,tene parameter howe l class howe l class le 3mele extend la class l jobintentservice bas b2lab l manifist file lezm nzeed permisiion esmo bind_job_servies bale ma bteshte8l she ,telet paramter howe job id integer howe 3ebra 3an id b7otot met ma badak uniqe , w e5er parametr heye l intent le ffek mn5elela tzeed aye intent b2lba data
fa hala2 mn 5elel hay l method we start the job intent service
hala2 badna nerja3 la wara la nshof sho l methods l be2ye le btetnfaz bel job intent service
fe method tenye esma onhandlework(intent):hay l method btestd3a mojarad ma stad3et method l enquework w bte5od hay l method intent le heye nafsa zeta l intent le nba3ate mn method l enqueu
w 3ande method telete esma onstopCurrentwork() btraje3 boolean hayde l method btem estde3e2a mojarad ma n3amal stop lal jobintent service btraje3 boolean ya3ne byen3amal callla hay l method nojarad ma l jobintent service ywa2efa l operating system hala2 iza raj3t true betkoun 3am t2olo lal operating system eno iza we2fet hay l servie bas te2dar rja3 sha8le hay service iza false betkoun 3am t2olo eno iza we2fet l service 3ade ma terja3 tsha8ele yeha
bel 2edafe la haw b7aje la permission tene esma Wake_clock badna nzedo bel android manfist file hyade l persmiion betse3dna b case eno l android system howe byeshte8l bshakl eno iza ma keen fe kteer she8l bel foreground l device la7alo byente2l la wad3 eno ya3mel stop la kel she w heda she enta ma badak yee yseer l2no enta 3am ta3mel she bel background ma badak ye ywa2fo l2no ne7na badna l job intent service dala sha8ale 7ata law ma fe work bel foreground 7ata law heye bel background heda sabab heda l permission
hyde l class le howe jobintentservice mawjoud bel android x support library
hala2 ne7na heda kolo 7ake lesa ma fhemna she hala2 badna nfout bel code w nefham akatr


fa hala2 badna na3mel nafs code le howe taba3 tsha8el l music bas badel ma na3mel extend la intentservice 2aw la serviece badna na3mel extend la jobintentservice
fa b class l service mna3mel extend la jobintenet service fa byotlob menak eno fe method lezm ta3mla immplimentaion la method esma onhandle work method heye similar la method le esma onhandle intent le mawjoude bel intentservice fa l code le ezm tektobo bel onhandle intent byenkatab bel ongandlewrok 2aw l code le keen yenkatab bel onstartcommand byenkatb be onhandle work l code le byetnafaza bel onhandle work byetnafaz bel worker thread bel clas l intentservice
w ba3d heek bade 2a3ml ovride la method esma onstopcureent work le heye bta3ml return la boolean hayde l method by deffult bteb3at true le heye bt2olo la system eno iza tam taw2ef hay l service la7la rja3 sha8ela w 5alena n7ot feha log stopcureentwork
hala2 ne7na 5aslna l immplemntation fa fe eshay lezm na3mela le heye keef badna nsha8ela 2olna badna nsha8ela best5dem method esma enqueu() heye btfawet 3ama m3yan b2lab queue fa hala2 bel service badna na3ml method esma enqueuwork() bte5od context w inetnt b2lab hay l method bade estd3e method le esma enqueuwork w 2a3teha l context le bado yejene mn hay l method le esma enque w l inetnt kamen l2no hay l method mawjoude b2lab l class heda ma fene esta5dema mara fa 3ashn heek 7atyta b2lab method 3ashn 2e2dar et3meal ma3a mn bara
hala2 mojarad ma nest3mel hay l method le enqueu hayde bte7res eno ydal 3amalak sha8al ya3ne ydal service sha8al mn 5elel l wake_clock le howe bey7res eno l service dala sha8ale b2lab tag l service ta3le fe tag esmo permission b7et b2labo permisiion la hyde service la ta3mel android:permission="android.permission.BIND_JOB_SERVICE"
w bara b7ot permission esmo work_clock
hala2 badna nsha8el l service fa mnrou7 3al activity mna3mel intent w bestd3e l method le 3mlta e esma enquee work w ba3teha l intent, context
 wake lock is a mechanism that allows your app to keep the device's CPU or screen active, even when the user is not directly interacting with it. This is useful for tasks that need to continue running in the background without interruption.

The permission android.permission.BIND_JOB_SERVICE in your service declaration is essential for using JobIntentService.
Purpose: This permission ensures that only the Android system or apps with this permission can bind to your JobIntentService. It acts as a security measure to prevent unauthorized access and control of your background service.
The BIND_JOB_SERVICE permission is mandatory for JobIntentService to function correctly.

hala2 rje3t ana tab2t nafs l exmole le bya3mnel generate random number l2no heda l class ma besh8el music le sar eno ana kabst start service fa shta8let service iza tle3t la bara w 2a3dt kteer wa2t mesh 7a ttefe ela w 7ata iza tle3t bshakl kemel mn l app kola
bas fe she sar eno ana 3am ekbous stop service mesh 3am to2af l service 3am dala sha8ale lesh?
w iza rje3t kaabst mara tenye start service sho 7a yseer?

ana fene 2a3mel sha8le eno iza l user kabs aktr mn mara 3ala l start button sho yseer fene 7ot b2lab l intent vlaue 7ot feha eno kel ma l user kab mara 3ala l button eb3at intent w b2laba yzeed l count ++count ya3ne 2awl mara ba ekbesa 7a tkoun 1,ba3den tene mara 2 .,.etc
hala2 b method l onhandlework bade b method l onhandlework est2bel l intent le nb3at feha l count w eb3ata l intent l value la methid l startrandomgerator w etba3a bel log cat les ar eno kabst 2awl mara keen l count 1 rje3t bas 3am ekbous mara tenye 3am ydal l count 1 ma 3am yet8ayr abdan sabab eno bel startgenerator method l code le b2lbo 3am yeshte8l b while loop infinte loop fa enta bas 3am tekbous mara tenye l startgenerator mesh 3am yet3dal 3lyha l2no heye b infinite loop  fe la7 yseer 7a 5ale loop temshe 5ams marat w ba3d 5asm marat la wa2efa w 2olo is kenet stoped tb3le l heda l count  ya3ne ana bsha8ela 5 marat generator ba3den bwaefa w bshof iza heye kenet wefe tb3le l id raja3 bel mara tenye bas terja3 tet8at 7a yet3dal l count w yentaba3

hala2 bel nesbe eno lesh ma btwa2ef servies bas ekbous 3al button l2no bas net3emal ma3 l jobintentservies hay serves ma btwa2ef ela la ta3mel stop self ya3ne ta3mel stop self bas twa2ef sho8la ma btwaef la7la bas t5les sho8la bedala meshye w momken hay l servies tetwa2f la7ala bas heda she bye3temed 3ala sho byen3amal return b method l onstopcurrentwork  iza true bterja3 la7ala bt3mle restart w btemshe iza flase bt2olak ma terja3 tsha8ela yeha fa bel mainactivity bas 2a3ml click 3ala stop button  ma 7a twa2ef servies l2no theres no way to stop jobintentservies jobintentservies will always run silent in background w ma fe 7ada ywa2efa iza 7at stopservies  w byen3mala stop ela iza n3ml call lal stop self le ne7na 7atyneh eno bas tkmel l she8l ta3elak 3mela stop fa ma fene 23mla stop yadaweyan ela iza heye n3mala stop la7la b certain setution momken bas da2ele hay l 7ale

hala2 bel nebe lal id le 3am neb3to bel enuque 7atyne 101 hal mne2dar kel mara n8ayro heda l id kel ma na3mel intent yet8ayr heda l id iza jarbt t8aro la7 ya3mel eno l id l equll kaza masln 102 is different from previous id 101 fa 7a ya3mel crush fa l id lezm ykoun sebet kel jobintentservies 3anda unique id

w e5er note eno l intentjobservies la7 dala run 7ata law tel3na mn application w 3mlna kill lal app bas 7a twa2efa she8la la7 twa2ef la7la bas hay eza jarbenha 3ala l emulator ok byemshe l 7al ama b real device heda she ma byemshe l 7al l2no b real device bekoun aktr restriction 3al background servies bas momken hal she yen7al b tare2a esma work manger



hala2 badna nrou7 3ala she esmo JobSchedular:
sho howe l jobschedular?
2awl she mn zamen keen 3ana she esmo bel android alarm manager:heda 3ebra 3an class bas sarlo deprecation fa keen heda l class wazefto eno enta ta3mel trigger 2aw tnafez 2aw toser 3amlyet mawjoude b wa2t mo3yan 2aw 5elel mode mo3yane nwade7 akatr
lyoum ana bade 2a3ml application heda l application bado y7ades news akbar kel 24 se3a fa l alarm manager brou7 ba3mel code taba3e jowa servies w beb3ta lal alarm manger fa l alarm manger berou7 kel 24 se3a byestd3e servies tab3te
exmple tene bade 2a3ml code mo3yan yeb3at notification kel youm se3a 5amse msln 2aw ykarer l 3amlye kel mode mo3yane 2aw be mode m3yan ya3mel fa brou7 best5dem alarm manager sha8le bteshbah l alarm le mawjoud bel telephone
fa fekret l alarm manger eno sha8le bt5lene nafez 3mlyet 5elel fatra mo3yane 2aw bkarera la fatra m3yane 2aw bnafeza ba3d fatra mo3yane

hala2 ba3den la2no en class l alarm mnager byeshlek resource mn device kteer byesthlek battray w ram fa wajadolna sha8le esma l jobschedular

jobschedular:howe 3ebara 3an class le jadwalet 2a7des 2aw 3amlyet "trigger events" ma3neta 3ande 7ads mo3ayn be nafzo. heda l 7ads benfzo  bwa2t m3ayn la mode m3ayen 2aw l both ma3 ba3d w bene2an 3ala shrout m3yane maln bade 2a3ml update la she m3ayn kel 24 se3a fa be2dar 2a3mela b2olo masln 3mlyet ta7des bade yeha tseer ba3d se3a mn tash8el shshe hayde msln 2aw tasker shshe hayde
l fekra mn l calss heda le bemyzo 3an l alarm manger    eno be7ewel adr l emken yesthlek resource b2a2al kamye momkene ya3ne ma byes7ab mn battary kteer wala mn ram kteer ma byet3eb l device le ne7na sha8len le nzel 3lee l app
bel2edafe eno be5lene edmej been majmo3a mn l 2e3dadet 2aw shrout m3ayen la 2a3mel 7adas m3ayn ya3ne ana bade 7ades l news kel 24 se3a fa momken 2a3te mawjmo3a mn shrout le heye btkoun bult in mawjoude bel class fa enta bt7adeda bt2olo maln kel 24 se3a w ykoun l device mashbok 3ala l charge w ykoun fe internet 7adesle l news maln fa best5dem method m3yane bte7le eno 2a3mel jadwale la 7adas bene2an 3ala shrout mo3ayne

ta3emoul ma3 class l jobschedular baset kteer howe 3ebara 3an servies ya3ne bel 2e5er l job schedular 3ebra 3an servies bya3mel jadwale la she esmo job servies job serves heye asln jobservies laken mohy2a eno tet3emal ma3 jobschedular le bya3emal jadwale  5elel fatra mo3yane

fe 3ande 3 methods mawjouden jowa l jobserves:
onstartjob:
jobfinished:
onstopjob:
5alean nshofoun 3amalee

fa 2awl she bade 2a3mel class  3ebra 3an servies bas naw3a job servies bade same MyjobServies bade 2a3melo extend mn class esmo Jobservies heda l job servies howe le 7a net3emal ma3a
fe 3ande jobservies w jobintentservies

fa ba3d ma 3mlt extend la heda l class byotlob mene 2a3mel immplemnt la 2 methods
onstartjob():system telka2ayn byestad3eha 2awl ma tblesh l 3malye l jadwale
onstopjob():system berou7 la7lo byestd3eha telka2yan bas yetm el8e2 l 3amlye 2abl ma tenthe fe far2 tel8e ya3ne job sha8le w enta bel nous la8yta berou7 byestd3e l onstopjob() bas tentehe l 3amlye berou7 ana le programmer bestd3e l job finshed bas tentehe l 3malye bestd3eha la7ata ya3ref l system eno ntahet l 3amlye
ya3ne ana 3ashn bale8 system eno ntahet l 3amlye brou7 bestd3e l jobfinshed()
2awl mola7za bade efhama eno haw 2 methods mesh ana le bestd3ehoun call backs system byestd3ehoun
5alena na3mel log 3ala kel method mn onstartjob(),onstopjob hala2 l mafroud 3ande 2 logs wa7de ta3et start wa7de ta3et stop
alak fe mola7aza houn lezm tentebhla l onstartjob(),w l onstopJob() berja3o boolean true or false
aymta ana braje3 true aymta false:
momken l code l 3amlye le badak tnafeza le badak tektoba bel onstartjob() tnafzo bel background
5ale belak   kalem heda jobservies w jobintentservies byetbe2 tameman 3ala l kalem le nshara7 b mawdo3 l servies houne haw bel nheye servies bas ela namat mo3ayn bel tash8el bteshte8l kel fatra m3yane bteshte8l kel mede m3ayen 2aw ba3d mede m3ayen bteshte8l bene2an 3ala shrout m3ayne hay le btefre2 3an servies l 3adeye ama kalem 3an servies l 3ade byentebe2 3lyha
2lak l code le momken tektobo bel onstartjob() nafs ma kenet tet3emal ma3 serveies momken ykoun code bye5od wa2t tawel 2aw code bye5las bsor3a ya3ne momken ykoun code bye7tej thread monfesle w momken code ykoun 3ade ya3ne codak le badak tektbo bel onstartjob() momkne ykoun b thread 2aw aync tasks w momken tektobo mobahsratan
2alak law enta katbto lal code mobahrtan fa enta badak tou7 ta3mel return false ya3ne l 3amlye ma bte7tej wa2t tawel fa rou7 nafez l 3malye le b2lb l onstartjob w 3mel return false iza kenet l 3malye ma bte5od wa2t tawel 3mel retrun flase 7a yefham eno ntahet l 3malye bas ta3mel retrun false
bas law l 3amlye btetwa23  eno bte7tej wa2t tawel  fa houn badak tsha8el thread b2lab l onstartjob() menfesle w trou7 t7et l code le badak yeh b2lba w ta3mel return true lesh 2a3mel retrun true 3ashn 2olo ya system ya nezam 5ale belak  l 3malye tab3te ana ba3ta b thread monfesle fa lisa ma 5alsta fa ba3melak retrun true 3ashn ma twa2ef ela lama 5ales
7a erja3 3eed return false ma3neto eno ana 5alst wa2ef   l servies tab3te
retun true ma3neta eno ana lisa ma 5alst fe thread sha8le fa retrun true ma twa2ef system la 7ata ye5las l thread she8la kelo
fa ya3ne iza badak test3mel thread b3at true iza ma badak b3at false

tyeb neje lal onstopjob():
hyde byestd3eha system iza l job lta8et mesh ntahet 2abl ma tentehe ya3ne fe she la8eha abl ma tentehe
exmple:ana l youm 3mel code be7des data bel background faj2a sar aye she mo3ayn ada la telte8e servies
fa hayde btraje3 kamen booolean heda l boolean bt2olak hal lama telt8e servies 2wl ma telte8e serrvies 7a yestd3e l method hay mesh lama t5les servies la2 lama telte8e fa 2wl ma telt8e abl ma t5les sho8la sho bado yseer
iza raj3t return true fa b2olo lal system iza sar she w lta8et l servies abl ma tenthe rja3 3mele ela restart
iza rej3t return false fa ana b2olo lal system iza sar she w l t8ate l servies abl ma tenthe l8eha ma terja3 tsha8ele yeha
fa enta law 7eses eno l 3amlye tab3tak bte7tej fe 7al l ta8et eno teshte8l mara tenye rou7 3mel return true enta ma bt2ser 3leek iza lt8et rou7 3meel retrun false

tab3an jobfinished 2olna eno ne7na mnestd3eha bte5od jobParameter object mn job fa momken bel onstart 2olo bas t5les 3meel call lal jobfinished w b3tala l parameter jobPrameter
w be3tla l paramter tene boolean eno once reschedule true 2aw flase ya3ne badak terja3 testd3a ba3d ma tenthe yes or no
hay l method  betsd3a lama 2olo eno 5als ntahet l method sho8la
ya3ne ana 3mel thread monfesle w bas t5les l thread bade 2olo enhe heda job l sha8al fa bestd3eha w be3tla l jobparameters w boolean iza enta 7ebeb terja3 tsha8ela mara tenye 2awl ma tentehe
JobParamter le be3to lal finished:heye object bekoun feha ma3lomet 3an servies le sha8ale
boolean wanttoresudeld:hal enta badak ta3mel jadwale mara tenye wala 5als badak tenheha

fa hala2 bade 2a3mel tajrobe sha8ela abl ma 2a3ml exmple l music fa 3ande butto startservies w button stop servies
la sha8ela lal servies ma best3mel startservies w intent la2 houn l mawdo3 bado jadwale  :

fe 3ande class esmo ComponentName:ba3mel meno obejct heda l class mn 5elelo b3ref l job servies eno heye commponent heda l object bye5od menak l context w bye5od menak esm l class taba3 servies
howe heda l class berou7 be3ref system eno heda l jobscheduled le bade eb3to 3ebra 3an commponent fa t3mela ma3o k2no commponent

bas houn fe sha8le nseneha class l job servie howe 3ebra 3an servies fa lezm n3arfo bel manifist ma byet3araf mn teleka2 nafso howe l2no mano jehez ne7na 3mlne
w lezm 2a3te la heda servies b2lab l manifist permission esmo bind_job_servies heda l permission ejbare t7oto b2lab l servies 3ashn te2dar teshte8el hay servies bte5od permission esmo bind_job_servies

hala2 ba3d ma 3mlt commponent name w 7atet fe l servies w l context
l 5otwe teneye 3ashn sha8el l servies hay bade 2a3ml object mn class esmo jobInfo
fe 3ande class esmo jobinfo:heda l class mn 5elelo b7aded l ma3lomet le bade yeha ne7na 7kena eno l jobservies 3ebra 3an class ana ba3mel fe l code taba3e tyeb jobinfo ana b7aded fe shrout le bade yeha w l wa2t le bade yeh w l mode le bade sha8ela mn 5elo ya3ne b7aded eno hay servies sho l shorout w l wa2t l mo3ayn la teshte8l kel adesh bade yeha teshte8l mn 5elel heda l class

fa lezm 3 sha8let 7ota bebele l jobservies 3ashn 2a3ml servies ,jobinfo 3ashen 7aded l shrout ,l jobschedular 3ashen 2a3ml jadwale la hayda l servies

fa ba3mel hala2 object mn jobinfo j b2olo equll new  JobInfo dot Bulder w b3te l job id le heye id serives,w tene parameter ba3te l commponent name le 3mlto hye l commponent name sha8le bteshabh l intent fena mn 5elal nosla mn actiity la activity mn activity la servies nsha8el servies metl ma shofna bel bound servies

hal2 ba3d a=ma 2a3ml job info ba3mel l shrout le bade yeha msln :
setperiodic():hyde bte5od millisec ma3neta iza 3tyta msln 5000 ya3ne ba3d 5 sawene rou7 sha8el servies
setminimumlatency():heyde nafs set peridic bas wa7de ba3d l version noga wa7de 2a2al mn version noga
setrequrenetworktype():ana basln bade 7aded sho lezm ykoun no3 l shabke la teshte8l l servies fa msln b2olo Jobinfo.network.any ya3ne lezm ykoun shebek 3ala aye shabke mawjodue
setcharged():ya3ne hal lezm la teshte8l servies ykoun ma7tot 3al charge msln ....ect
w 2e5er she ba3d ma t5led l shorout le badak yeha bt2olo .bulid() fa berou7 bya3mel build lal inforamtion l maawjoude 3ande w byorbota ma3 l componentname heda

hala2 ne7na 2olna fe 3 sha8let ne7na jahzan l jobservies ,jobinfo hala2 bade rou7 2a3mela jadwale b2olo:
Jobshedular  schedilar=getsystemservies(Job_schedilar_servies) ya3ne 3atene l servies le esma jobschedular servies w ba3mela casting fa howe ra7 jeble hay servies mn syetm w ba3den b2olo 3mele jadwale
schdular.schedular(jobinfo):b2olo rou7 3mele jadwale lal job info le 3ande ya3ne lal servies tab3te fa 7a yrou7 yjadwel hay servies b2lab l systemservies 7asb l baynet le enta 3ate yeha bel jobinfo yjadwela y3ne ysha8ela b shrout m3ayen

neje houn badna nwade7 ba3d l medthods le bel jobinfo:
setminimumLatency,setperriodic():setperiodic() hyde bteshet8l law keen l version 2a2l mn 24 le heye noga law l version 2a3la mn l 24 2a3la mn noga so setminimumlatency


hala2 bade tabe2 exple l music
w bas 3lna run 3ade shat8alet w metla metl jobintentservies bedala sha8le 7ata law tel3an mn app bas btetfe iza 3mlna kille lal application w heda she 7a y7olo l workmanger

workmanger:
hala2 ne7na darsna servies,intentservies,jobservies,jobintentservies,foreground servies bas la na3ref aymta nest5dem kel wa7de heda se sa3b fa 3mlolna android she esmo workmanger hyade 3ebra 3an api 2a2wa men kel le mara2o heda l work manger mawjoud bel android x

5alena hala2 nshof sho howe l work manager?
eza shofna ta3ref rasme be2olo eno:work manger is an api that make easy to schedule deferrable,asynchronous tasks that are expected to run reliably
ya3en howe api b5alena bshakl sahl jadwalet l mahem l 8eer kabele lal ta2jel ,w l motawake3 eno teshte8l bshakl mowasak

asynchrounos:ya3ne she byeshte8l bel back ground w bekoun syncronize
deferrable:ma3neta she m2ajal ya3ne bshakl tene eno lezm tkoun 2eder 3ala l 7osoul 3ala eleyet mo7kame kteer l jadwalet l tasks le 3andak le heye iza keen badak tsha8el heda task mara wa7de bas heda l servies  2aw motakarer w kamen bekoun motawefe2 ma3 wad3 l jehez l android leheye malsn iza enta badak tsha8lea bas ykoun 3ala l power saving mode 2aw bas ykoun malsn shshe tafye ya3ne metl wa2t 3mlna bel jobinfo sorna n7ot eno badna yeh teshte8l kel 5 sawene  2aw periodic
reliably:ma3neta eno b nafs l wa2t feek t7aded constaint la hyde servies ya3ne eno l servies hay teshte8l bas iza keen l wifi on,2aw when the device idle ,2aw iza keen fe suffeciant storage nafs tare2a wa2t 2olna bel job info eno badna yeh ykoun 3al charge 3ando network type kaza heek
fa enta 7a te2dar mn 5elel l workmanager tet7akm b hay sha8let w teshte8l servies le 3andak background bene2an 3ala haw l shrout le m3yane
w de2man 7a te2dar ta3mel finish lal task ta3etak even if app exits even if device restart heda she mhem l2no enta badak tet2akd eno l task completed 7ata law l app exits 7ata law 3mlna restart lal device


hala2 5alena nshof keef fena nest5dmo sho befrna la nest5dmo:
hala2 la nest5dmo badna net3emal ma3 she esmo worker heda badna na3mel class extend heda l class l worker b2lab heda l class la7 t7aded sho heye task le badak tsha8ela bel back groudn metl random number music..etc bas ta3mel extend la heda l class la7 ykoun 3andak ba3d l call bakck methods metl l dowork(),onstoped() hawde 2 methods call back system bya3meloun call back l dowork btestd3a bas tblesh l task w l onstop btem estded3e2a bas telte8e l task faj2a ya3ne mnkoun b nousa bseer she m3yane fa btelte8e l task metl malsn iza ne7na 7atyna eno hay task ma bteshte8l ela iza fe wifi fa ne7na sha8lneha w keen fe wifi fa faj2a ra7 l wifi fa hay l servies btelte8e heye w bnousa abl ma tkamel 3amala
ba3den 3andak class constaint heda l ma7al badak ta3mel b2labo t7aded l containet ta3el hay servies metl eno hay servies ma bteshet8l ela iza ken device 3al charge ela iza keen fe wifi..etc b2lab heda l clas bt7aded bt3ref l conatinet
ba3den 3andak class telet esmo workrequest hayde heye bel ases api bte7tewe 3ala kteer mn l mokawenet 3ala sabel l mesel example iza kenet badak tsha8el hay servies mara wa7de w ba3den tetfeha iza keen badak yeha tblesh ba3d 5 sawene metl ontimeworkrequest,periodicworkrequest
e5er she badak tet3eml ma3 class l work manger l work manger byest5dam la ya3mel enqueu la work ta3elak b2labo howe 3ebra 3an moder benzem task bya3mel la task enque b2labo   2aw mn 5elel badak ta3mel cancel la task le howe under expection ya3ne howe enqueueing the work,cancelling the work

hala2 badna nafez heda l kalem
3ana activity b2lab 2 button wa7de start servies wa7de stop servies w textview

-2awl she lezm deef depenecy ta3el l work manger la ne2dar nest3mel l work manger
-second step metl ma 2ona mna3mel calss msln esmo randomnumbergenerator extend work class heda l class le badna n7ot fe task ta3etna random number fa fe 2 method byen3mloun overide dojob stopjob fa ne7na mna3mel sho8lna b function w bl2b method l start beb3t l method heda l maken b2lab function dojob le 7a tyem tanfez l code tab3an heda l class 3ando constrctor bye5od context w workerparamter w bya3tehoun la super fa ana b3ref 3ande content w workerparamter b2lab method l dowork lezm raje3 2eme mno3 object esma result fa brjae3 object now3o Result.succes():hyde 3ebara 3an class esmo result b2lbo function eso succes retry failer hyade bt3ber eno sho bado yseer bel background ya3ne mn 5elel bas ta3mel return la hyde fa enta 3am t2aked eno l work 7a ytem benaje7 ya3ne job will completed succefully ya3ne 3am t2olo la system eno 3meel random varibale ba3den b3at hayde fa 3am t2olo eno system 5als she8lo benaje7 hala2 feek teb3at faliler sa3eta 3am t2olo eno system falier ma 5als she8lo iza 2oltelo retry fa system 7a yerja3 l jadwel hay l 3amlye w ynafez mara tenye fa ana heek 3am 2olo nafez generate random number w ba3den bas t5les btkoun ntahet l 3malye fa feek tenhe servies 3al ba3mel overide lal onstoped hay method btestd3a iza l ta8et l 3amlye
hala2 ne7na5alasne lal class hala2 kef badna nsha8el l servies beste3mel class esmo workmanger le benfez l 3mlye le mn5elelo ne7na mna3mel enque lal work 2aw cancle lal work w best3mel l workrequst le badna ndeef mn5elelo constrianet la hay servies
fa mn3ref 2 object global bel main wrokmanger,workrequst
workManager=WorkManager.getInstance(getApplicationContext()); b2lab class l workmanger fe method emsa getinstance btrje3le object mn l workmanger w bte5od context
hal2 bel nesbe lal workrequst:
 workRequest=new OneTimeWorkRequest():hyde iza keen bade 23ml service mara wa7de bas
 iza bade yeha persiodic:  workRequest=new PeriodicWorkRequest()
2aw feek ta3mel new WorkRequst() w seer zeed paramter le bade yeha
ba3den ba3mel dot bulder la yensha2 heda l periodic w tene paramter eno kel adesh badak ya3mel periodic msln kel 15 min fa bt7aded eno timeunit in minut ba3den dot iza badak t7ot constainet setconatinet ,setbackoffcraiteria,setdelay w e5er she bt7ot build 3ashn yen3am build

hala2 ba3den la 2a3ml start la hay servies best3mel method bel workmanger esma enque w fee kteer 5eyarat la enque fe enqueperiodicwork,enqueuniquework
w hayde bte5od l l workrequest
hala2 la 2a3ml stop lal servies mn 5elel method esma cancel fe cancellallwork bhay l workmanger  2aw fek ta3mel cancel la wrok m3yane bhay l workmamnger mn 5elel id w btjeb l id mn l work requst workrequst.getid()

hala2 3mlt run 3mele generate la 5 marat w rej3 stoped the servies l2no ne7na raj3na retun succefully

bas badna nentbh eno l wa2t le bet7oto la terja3 servies teshet8l lezm minimum 15 min l workermanger b5ale task tkamel she8la 7ata law kill the app

hala2 ne7ke mawdo3 tene
senarios:
senario1:t5ayal 3andak 4 worker w badak ya3mlo excute wara ma3d ya3ne bas y5les l worker 1 yblesh tene ba3den basy5les tene 3 ..ect
senario2:2aw senario 2 3andak worker 1 badak 2awl ma t5les l worker 1 tblesh l worker2 w lworker 3  w badak bas y5lso she8loun l worker 2 w lworker 3 badak yblesh worker 4
senario 3:badak yblesh worker 1 bas y5les worker 1 yblesh 2 bas y5les 2 yblesho ma3 ba3d l 3 w 4
senario 4:badak yblasho worker 1,2,3 ma3 ba3d w bas y5lso koloun yblesh worker 3
...ect fee kteer heek senario
haw senarios keloun bye2dar l work manager ynafezoun
fa ne7na badna net3malm eno keef fena na3mel l worker b proper order fa eno ykoun 3ana controle eno keeef iza keen 3na majmo3et worker yetnafazo
worker manger 3emel heda she bhskl kteer easy w heda she byetsama b work chaining

fa 5alena nblesh bel implemntation:
fa hala2 bade 2a3mel 3 worker l 3 worker metl ba3d 7a ykono bas le bye5telef eno 7a 8ayer l asme2 bel logcat
hala2 bade 2a3ml 3 worker request i use one time request w bade kel wa7de zeed 3lyha tag esm l worker lesh l2no fena nzeed tag byo5od string mn 5elel heda tag fek tsha8el l worker w tafeha

w 3ande work manger bedalo metl ma howe
hala2 l different eno keef bade 2a3ml enque sho tare2a
bade 2olo workmanger dot beggin with ba3te 2awl workrequest ba3den b2olo then ba3te request2 berja3 b2olo then request 3 ba3den b2olo enuq ya3ne rou7 nafez l 2awl ba3den bas t5les nafez tene ba3den nafez telet


ba3den la 2a3ml cancel best3mel tags le 7atytoun

w 2alak ma feek ta3mel heek tare2a ela iza keen no3 request ontime

tyeb barke badak ta3mel esno yesht8lo l worker 1 ,w 2 ma3 ba3d w bas t5lso teshte8l l 3
hyne btest3mel array eno bt2ol beginwith array wbt3teh l 2 worker w then e5er worker

bhay tare2a worker 1,2 7a yemsho paralel w bas y5lso byemshe l worker 3

tyeb hala2 iza kabst stop servies fa le 7a yseer eno worker 3 ma 7a yen3amalo exection
tyeb barke 3mlt cancel lal worker 2 fa le 7a yseer 7a ye3amal cancel worker 2 w worker 3 never exection l2no worker 3 7a yen3amal exection after worker 1 w worker 2 w ne7na 3mlna cancel lal worker 2 fa ma 7a ta3mel exection lal worker 3
l2no l worker chain b2olak iza nla8a l worker m3yan w ken fe worker ba3do fa heda l worker ma 7a yen3amlo exection


hala2 badna netl3lam eno keef fena nest5dem l workmanger la na3mel long running tasks:
la na3mel heda she badna nest3mel nafs concept ta3el l foreground
ya3ne b ma3na t5ayal 3ana application run bel background it run periodically 2aw one time fa 5elel heda l wa2t lezm 2a3mel show la notification to indicate that something is happen in background so 3am n2ol ya system hay 2awlaweta 3alye fa ma tel8eha l 2no l user 3am yest5dema marbouta b ui w t5ayal eno hyde notification la nazel masln file 2aw she fa bas ye5las donwload l file lezm l notification ye5tefe ya3ne kteer byeshbah l foreground servies le byefre2 eno hala2 badna nest5dem l work manager
tyeb keef fena na3mel using work manager aw eno howe to trigger the notification when worker start?

fe 3ana class esmo foregroundinfo heda class holds info hold notification that we want to trigger mna3mel meno object
ba3den ba3d heek badna nest5dem method esma setForegroundAsync() bhay l method lezm  7ot feha l foregroundinfo le heye feha notification  hay l method byen3amal call mn 5elel l dostart method le majoude bel worker hayde btet3mal iza 3ana long task runing in background
ba3den bel manifist file lezm na3ml add  service tag b2alba bel name eno 2olo hay foreground sevice ba3den lezm 7aded service type microphone|sevice  2aw data sync,w tools ="merge"

hala2 badna nbalesh bel immplement:
l work class bedalo metl ma howe w bel mainactivity bedal nafs l code workmanger,workrequest w kamen mna3mel enque lal workrequest
heda she le mn3rfo bel manifist heda 3ebra 3an servies mawjoude bel system l work manger byest3mlola ya3mel communication ma3o w attribute le esma node merge hayde 3ashn ma yser confilct l2no barke fe 3ande 7ada tene application tene 3am yset3mlo fa an b2olo merge w nou3 servies datasync hyde ma3neta indicate this servies related to background data syncronization fek t7ot location l badak yeh bas mohem eno t2olo hay foreground servies ela no3 3ashn bel android 14 sar lezm t7aded nou3 servies

l change le 7a yseer eno bade 2a3mel notification bade 2a3mel class esmoMyappnotification
fa ana ba3mel notification method ba3d ma 5alesa ba3mel method tenye bt3mel return la foreground if hayde bte5od notification id w bte5od notification fa ba3mel b2lba estd3e2 la method notification w ba3mel retrun la w b2lab notification w id notification w b2lab l do work bestd3e method l  setforegroundasync hayde bte5od foregoundinfo fa b3mel return la method le btrja3a w b2lab notification w b7ota b2ba hayde l method bt5le l system ybeyn notification w trigger notification bshakl eno bas yentehe servies l system recognize eno l servies 5elset generate 20 random number fa bas te5la sl servies byen3amal call la method emsa stopforeground fa hyde bta3mel stop lal foreground syn le fe b2lba notification fa l work manager recongize eno 5elset serives fa 3meel class la hay l method w 3ateha notification id le b2lab l foregroundasyn fa btel8e notification
ya3ne l stopmethod byest3mel l foregroundinfo le 3mlta le b2lab l notification w l notification id fa bye5od l id w byem7e l notification


ya3ne ne7na 2awl she mna3ml run la servies fa l method l setforegroundasyn hyde btest3mal la 2oloa system hayde foreground servies fa ma tel8eha ya3ne byen3aml b2lab long task fa ana b3tha la hayde l method foregroundinfo le feha notification w notificationid l work manager communicate ma3 l systemforegound servies le mawjoud bel android system le howe 3arfen bel manifist w hyde l method bterselo la heda l systemforegroundservies le mawjoud bel android bt3te l foregoundinfo le feha notification fa ne7na bas nestd3e l hayde l method btersela w btkoun systemforegroundservies sha8al fa btsha8el l notification ba3d heek bteshte8l l genrate random number f bas kel l work le b2alb l dowork yentehe w na3mel return succes fa l systemservies le bel manifst howe 3anod signel 3alal workmanager fa bya3ref eno 5elset l dowrok fa bya3mel call system lamethod internally esma stopforeground w system 3ando l foreground info ne7na ba3tenoloyeha bel foregroundasyn fa byest3mel foregroundinfo le feha notification bye5od mena l id w bya3mel remove lal notification
w hay kel she 5aso bel workmanger
 */

public class
MainActivity extends AppCompatActivity {
    int count;
    Button Start_service,Stop_service;
    ActivityResultLauncher<String> activityResultLauncher;
    WorkManager workManager;
    WorkRequest workRequest,workRequest1,workRequest2,workRequest3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Start_service=findViewById(R.id.start_service);
        Stop_service=findViewById(R.id.stop_service);
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean o) {

            }
        });

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);

                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS},1);
                    }
                }

            }

            workManager=WorkManager.getInstance(getApplicationContext());
            workRequest=new OneTimeWorkRequest.Builder(RandomNumberGeneratorWork.class).addTag("worker 1").build();
            workRequest1=new OneTimeWorkRequest.Builder(RandomNumberGeneratorWork1.class).addTag("worker 2").build();
            workRequest2=new OneTimeWorkRequest.Builder(RandomNumberGeneratorWork2.class).addTag("worker 3").build();
            workRequest3=new OneTimeWorkRequest.Builder(RandomNumberGeneratorWork.class).build();

        Start_service.setOnClickListener(v->{
//            Intent intent=new Intent(this, MyService.class);
//            startService(intent);
//            Intent intent=new Intent(this, MyService2.class);
//            startService(intent);
//            Intent intent=new Intent(this, MyIntentService.class);
//            startService(intent);
//            Intent intent=new Intent(this, ForegroundService.class);
//            ContextCompat.startForegroundService(getApplicationContext(),intent);
//            Intent intent=new Intent(this, BoundService.class);
//            startService(intent);
//            Intent intent=new Intent(this, JobIntentServices.class);
//            intent.putExtra("starter","starter"+(++count));
//            JobIntentServices.enqueuwork(getApplicationContext(),intent);
//            ComponentName componentName=new ComponentName(getApplicationContext(), jobservies.class);
//            JobInfo jobInfo=new JobInfo.Builder(10,componentName)
//                    //.setMinimumLatency(5000)
//                    //.setRequiresCharging(true)
//                    .build();
//            JobScheduler scheduler=(JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//            scheduler.schedule(jobInfo);
//            workManager.beginWith((OneTimeWorkRequest) workRequest).then((OneTimeWorkRequest) workRequest1).then((OneTimeWorkRequest) workRequest2).enqueue();
//            workManager.beginWith(Arrays.<OneTimeWorkRequest>asList((OneTimeWorkRequest) workRequest, (OneTimeWorkRequest) workRequest1)).then((OneTimeWorkRequest) workRequest2).enqueue();

            WorkManager.getInstance(getApplicationContext()).enqueue(workRequest3);
        });

        Stop_service.setOnClickListener(v->{
//            Intent intent=new Intent(this, MyService.class);
//            stopService(intent);
//            Intent intent=new Intent(this, MyService2.class);
//            stopService(intent);
//            Intent intent=new Intent(this, MyIntentService.class);
//            stopService(intent);
//            Intent intent=new Intent(this, ForegroundService.class);
//            stopService(intent);
//            Intent intent=new Intent(this, BoundService.class);
//            stopService(intent);
//            Intent intent=new Intent(this, JobIntentServices.class);
//            stopService(intent);
            workManager.cancelAllWorkByTag("worker 4");

        });

    }
}