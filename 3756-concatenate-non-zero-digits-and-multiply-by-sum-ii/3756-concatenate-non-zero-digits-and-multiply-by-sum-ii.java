class Solution {
    static final int MOD = 1_000_000_007;

    static class Node{
        long val;
        int len;
        Node(long v, int l){
            val = v;
            len = l;
        }
    }
    Node[] tree;
    int[] digits;
    int[] pos;
    long[] pow10;
    long[] prefix;
    int k;
    public int[] sumAndMultiply(String s, int[][] queries) {
        int n = s.length();

        ArrayList<Integer> p = new ArrayList<>();
        ArrayList<Integer> d = new ArrayList<>();

        for(int i = 0; i < n; i++){
            int x = s.charAt(i) - '0';
            if(x != 0){
                p.add(i);
                d.add(x);
            }
        }
        k = p.size();
        pos = new int[k];
        digits = new int[k];

        for(int i = 0; i < k; i++){
            pos[i] = p.get(i);
            digits[i] = d.get(i);
        }
        pow10 = new long[k + 1];
        pow10[0] = 1;
        for(int i = 1; i <= k; i++){
            pow10[i] = (pow10[i - 1] * 10) % MOD;
        }
        prefix = new long[k + 1];
        for(int i = 0; i < k; i++){
            prefix[i + 1] = prefix[i] + digits[i];
        }
        if(k > 0){
            tree = new Node[4 * k];
            build(1, 0, k - 1);
        }
        int[] ans = new int[queries.length];

        for(int i = 0; i< queries.length; i++){
            int l = queries[i][0];
            int r = queries[i][1];

            int left = lowerBound(pos, l);
            int right = upperBound(pos, r) - 1;

            if(left > right || left == k || right < 0){
                ans[i] = 0;
                continue;
            }
            Node res = query(1, 0, k - 1, left, right);
            long sum = prefix[right + 1] - prefix[left];
            ans[i] = (int) ((res.val *(sum % MOD)) % MOD);
        }
        return ans;
    }
    void build(int idx, int l, int r){
        if(l == r){
            tree[idx] = new Node(digits[l], 1);
            return;
        }
        int mid = (l + r)/ 2;
        build(idx * 2, l, mid);
        build(idx * 2 + 1, mid + 1, r);

        tree[idx] = merge(tree[idx * 2], tree[idx * 2 + 1]);
    }
    Node merge(Node a, Node b){
        long value =(a.val * pow10[b.len] + b.val) % MOD;
        return new Node(value, a.len + b.len);
    }
    Node query(int idx, int l, int r, int ql, int qr){
        if(ql == l && qr == r) return tree[idx];
        int mid = (l + r)/ 2;
        if(qr <= mid)
        return query(idx * 2, l, mid, ql, qr);
        if(ql > mid)
        return query(idx * 2 + 1, mid + 1, r, ql, qr);

        Node left = query(idx * 2, l, mid, ql, mid);
        Node right = query(idx * 2 + 1, mid + 1, r, mid + 1, qr);
        return merge(left, right);
    }
    int lowerBound(int[] arr, int target){
        int l = 0, r = arr.length;
        while(l < r){
            int m =(l + r) / 2;
            if(arr[m] < target)
            l = m + 1;
            else
            r = m;
        }
        return l;
    }
    int upperBound(int[] arr, int target){
        int l = 0, r = arr.length;
        while(l < r){
            int m =(l + r) / 2;
            if(arr[m] <= target)
            l = m + 1;
            else
            r = m;
        }
        return l;
    }
}