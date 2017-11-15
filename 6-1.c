#include <stdio.h>
#include <stdlib.h>
int a(int s,int d[],int f)
{
	int i,j,tmp;
	for(i=1;i<f;i++)
	{
		for(j=0;j<f-1;j++)
		    if(d[j]>d[j+1])
		  {
			  tmp=d[j];
			  d[j]=d[j+1];
			  d[j+1]=tmp;
		  }
	}
	if(s==1) for(i=0;i<f;i++) printf("%3d",d[i]);
	if(s==0) for(i=f-1;i>=0;i--) printf("%3d",d[i]);
	printf("\n");
}
int main()
{
	int s,f,i;
	int d[20];
	for(;;)
	{
		printf(" 輸入1或0:");
		scanf("%d",&s);
		printf("輸入幾個數字:");
		scanf("%d",&f);
		for(i=0;i<f;i++) scanf("%d",&d[i]);
		a(s,d,f);
	}
	return 0;
}
