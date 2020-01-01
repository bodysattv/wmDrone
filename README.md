# wmDrone
Walmart Drone Delivery


Delivery Satisfaction Problem
	Grid village
		each block 1 min
	10-6 delivery
	input file
		WM001 N11W5 05:11:50
		WM002 S3E2 05:11:55
		WM003 N7E50 05:31:50
		WM004 N11E5 06:11:50
		id.   VmHn  Time of Order
    
    
Assumptions
	       
   1. There is no advantage in maintaining any sequence within promoter's, neutral's or detractor's delivery.
		Basis: The points within a category/bin does not enter in NPS calculation. 
		Consequence: Input data can be tagged into three categories bins. These entries may have tag spill over during the hueristic calculation. Problem boils down to put as many entries into 'promoter' and then to 'neutral' from NPS pov.
			If total number of entries is N; p is number of promoters and n is number of neutrals, then NPS = (p-(N-p-n))*100/N = 100*(-1+(2*p+n)/N)

   2. Input is coming at the begining of delivery as a flat file(its not a stream), it is available just after the last order entry. And file time-stamp range is between 00:00:00 to 23:59:59 (belonging to the same day)
		Basis: Stream is not mentioned. In the example, the last delivery timestamp is after 6AM, so the file can be assumend to come at anytime before 10PM. 
Consequence: 
		Inital tagging.
		00----------03------06-------------------------------------------22----------24
	        <------>                                                        <------>   NPS=-100 Case  
			1. All orders 3 hours before last order are detractors. 
			2. last order if placed 3 hours before 6 AM, all are detractors.  
			3. first order if placed after 10 PM, all are detractors.

   3. There is no overhead time for the warehouse to pick the next delivery, bring to the launch pad and attach to the drone etc. 
	  Basis: Should have been explicitely mentioned.
    Consequence: No additional term to accumulate and calculate with. 
	4. Drone is picking one item at a time. 
		Basis: That is usual drone design else multiple complexity may arise about drone capacity, fuel, internal random access etc.
		Consequence:   Need not consider one more natural to measure density or order or reward recursive
    analysis and one that may work better with shift of origin and heuristic. 
   5. Drone is flying over the grid road (and not diagonally)
		Basis: Speed is mentioned over vertical or horizontal grid only. No flying regulation complexity mentioned. 
    Consequence: Data structure can be simpler: (X,Y,T,t). Combined with assumption 4, even direction does not need to be maintained 
    in calculation. Any delivery, for calculation purposes can be simplifed to two coordinates per order - .
    t = Time to get to its destination from origin
    T = Time of order relative to the start of delivery (last order time-stamp). 
    
    so if the delivery is done in this sequence: (t1,T1), (t2,T2), .... (tn,Tn)
    time of departture for kth delivery should be
    Sum over (i=0 to i=k) Ti

				4. All orders placed between 1-3 hour before last order are potentially nuetral.
			5. 


   total NPS = Deivery in 
   h<sub>&theta;</sub>(x) = &theta;<sub>o</sub> x + &theta;<sub>1</sub>x
&sum;<sub>i=0</sub>
