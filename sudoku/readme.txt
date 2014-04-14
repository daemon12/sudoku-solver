	1	*	3	*	5	6	*	8	*
	*	*	*	7	*	*	*	2	*
	7	*	9	1	*	*	4	*	6
	2	3	*	*	6	7	*	9	*
	*	*	*	8	*	*	2	*	4
	8	*	1	*	*	4	*	6	*
	*	*	5	*	7	*	*	*	*
	*	7	*	9	*	2	3	*	5
	9	*	2	*	4	5	*	7	*

===============================================================================

{0=0, 1=-1, 2=3, 3=-1, 4=-1, 5=2, 6=-1, 7=-1, 8=-1}		
{0=0, 1=-1, 2=5, 3=2, 4=-1, 5=-1, 6=-1, 7=-1, 8=-1}		
		
loop	from 1 to 9	
	for number=1	
loop	from circle 0-8	
	for circle=0	
	get all unknowns coords	0=1, 1=0, 1=1, 1=2, 2=1
	if number present in circle	go to next circle
	for circle=1	
	get all unknowns coords	
	if number present in circle	go to next circle
	for circle=2	
	get all unknowns coords	0=6, 0=8, 1=6, 1=8, 2=7
	if number not present in circle	continue
loop	over unknown coords	
	is 0th row or 6th column contains number=1	Y-eliminated
	is 0th row or 8th column contains number=1	Y-eliminated
	is 1st row or 6th column contains number=1	considered
	is 1st row or 8th column contains number=1	considered
	is 2nd row or 7th column contains number=1	Y-eliminated
		
if	size(considered)==1	value at coord = number




===============================================================================

R:	{0=-1, 1=7, 2=-1, 3=0, 4=6, 5=-1, 6=-1, 7=5, 8=2}		
C:	{0=3, 1=-1, 2=8, 3=-1, 4=-1, 5=7, 6=4, 7=1, 8=-1}		
			
	loop	from 1 to 9	
		for number=2	
	loop	from circle 0-8	
		for circle=8	
		get all unknowns coords	6=6, 6=7, 6=8, 7=7, 8=6, 8=8
		if number=2 not present in circle	continue
	loop	over unknown coords	
		is 6th row or 6th column contains number=2	Y-eliminated
		is 6th row or 7th column contains number=2	Y-eliminated
		is 6st row or 8th column contains number=2	considered
		is 7st row or 7th column contains number=2	Y-eliminated
		is 8nd row or 6th column contains number=2	Y-eliminated
		is 8nd row or 8th column contains number=2	Y-eliminated
			
	if	size(considered)==1	value at coord = number
			
