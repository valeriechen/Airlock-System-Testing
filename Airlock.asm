asm Airlock
	
import StandardLibrary
	
signature:

enum domain InCmdType = {CloseInDoor | OpenInDoor}

enum domain OutCmdType = {CloseOutDoor | OpenOutDoor}

enum domain ChCmdType = {ToInPres | ToOutPres}

enum domain Door = {open | close}

enum domain Chamber = {InPres | OutPres}

monitored mInCmd: InCmdType 

monitored mOutCmd : OutCmdType

monitored mChCmd : ChCmdType

controlled CInDoor : Door

controlled COutDoor : Door

controlled cChPres : Chamber

definitions:
macro rule  r_1 = if ((mChCmd = ToInPres) and (cChPres = OutPres) and (cInDoor = closed) and (cOutDoor = closed)) then
     
             cChPres := InPres

           endif

macro rule  r_2 = if ((mChCmd = ToOutPres) and (cChPres = InPres) and (cInDoor = closed) and (cOutDoor = closed)) then
     
             cChPres := OutPres

           endif

macro rule  r_3 = if ((mInCmd = OpenInDoor) and (cChPres = InPres) and (cInDoor = closed)) then
     
             cInDoor := open

           endif

macro rule  r_4 = if ((mInCmd = CloseInDoor) and (cChPres = InPres) and (cInDoor = open)) then
     
             cInDoor := closed

           endif
           
macro rule  r_5 = if ((mOutCmd = OpenOutDoor) and (cChPres = OutPres) and (cOutDoor = close)) then
     
             cInDoor := open

           endif
                      
macro rule  r_5 = if ((mOutCmd = CloseOutDoor) and (cChPres = OutPres) and (cOutDoor = open)) then
     
             cInDoor := closed

           endif

main rule r_SIS = par r_1[] r_2[] r_3[] r_4[] r_5[] r_6[] endpar
	
default init s1:   

  function mInCmd = CloseInDoor

  function mOutCmd = CloseOutDoor

  function mChCmd = ToInPres

  function cInDoor = closed

  function cOutDoor = closed

  function cChPres = InPres


   
