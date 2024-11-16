public int[] tiga(int[] height) {
    int n=height.length; 
    int[] ans = new int[n];
    Stack<Integer> stac = new Stack<>();
    ans[n-1]=0; stac.push(height[n-1]);
    for(int i=n-2; i>=0; i--){
        int count=0;
        while(!stac.isEmpty() && stac.peek()<height[i]){
            stac.pop(); count++;
        }
        if(stac.isEmpty()){
            ans[i]=count;
        }else{
            ans[i]=count+1;
        }
        stac.push(height[i]);
    }
    return ans;
}