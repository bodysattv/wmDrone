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
	1. There is only one drove to finish all delivery sequentially.  
		 Basis: There is no mention of number of drones or related aspects in the problem. 
     Consequence: There is parallel procesing of the order list needed. 
	2. Drone is picking one item at a time. 
		Basis: That is usual drone design else multiple complexity may arise about drone capacity, fuel, internal random access etc.
		Consequence:   Need not consider one more natural to measure density or order or reward recursive
    analysis and one that may work better with shift of origin and heuristic. 
   3. There is no overhead time for the warehouse to pick the next delivery, bring to the launch pad and attach to the drone etc. 
	  Basis: Should have been explicitely mentioned.
    Consequence: No additional term to accumulate. 
   4. Drone is flying over the grid road (and not diagonally)
		Basis: Speed is mentioned over vertical or horizontal grid only. No flying regulation complexity mentioned. 
    Consequence: Data structure can be simpler: (X,Y,T,t). Combined with 2nd assumption, even direction does not need to be maintained 
    in calculation. Any delivery, for calculation purposes can be simplifed to two coordinates per order - .
    t = Time to get to its destination from origin
    T = Time of order relative to the start of delivery (last order time-stamp). 
    
    so if the delivery is done in this sequence: (t1,T1), (t2,T2), .... (tn,Tn)
    time of departture for kth delivery should be
    Sum over (i=0 to i=k) Ti
   total NPR = 
   h<sub>&theta;</sub>(x) = &theta;<sub>o</sub> x + &theta;<sub>1</sub>x
&sum;<sub>i=0</sub>
