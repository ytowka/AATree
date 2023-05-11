package aatree;

public class AATree {

    private Node root = null;
    public Node skew(Node t){
        if(t == null){
            return null;
        }else if(t.l == null){
            return t;
        // Проверяем, есть ли у нас левое горизонтальное ребро
        }else if(t.l.level == t.level){
            // Меняем указатель горизонтального левого ребра
            return new Node(t.l.value, t.l.level, t.l.l, new Node(t.value, t.level, t.l.r, t.r));
        }else
            return t;
    }

    public Node split(Node t){
        if(t == null){
            return null;
        }else if(t.r == null || t.r.r == null){
            return t;
        // Проверяем, один ли уровень у родителя и внука, т.е. существует ли два последовательных правых горизонтальных ребра
        }else if(t.level == t.r.r.level){
            // Существует два правых горизонтальных ребра. Берем центральную вершину, «поднимаем» ее и возвращаем указатель на нее
            return new Node(t.r.value, t.r.level + 1, new Node(t.value, t.level, t.l, t.r.l), t.r.r);
        }else
            return t;
    }

    int insert(int value){
        Operation op = insert(value, root);
        root = op.node;
        return op.iterations;
    }

    public Operation insert(int value, Node t){
        int iters = 1;
        if(t == null){
            Node newNode =  new Node(value, 1, null, null);
            iters += 1;
            return new Operation(newNode, iters);
        }else if(value < t.value){
            iters += 1;
            Operation op = insert(value, t.l);
            t.l = op.node;
            iters += op.iterations;
        }else if(value > t.value){
            iters += 1;
            Operation op = insert(value, t.r);
            t.r = op.node;
            iters += op.iterations;
        }
        // Случай x == t.value не определен. Т.е. вставка не будет иметь никакого эффекта,
        // возможны различные варианты обработки, в зависимости от решаемой задачи
        t = skew(t);
        t = split(t);
        iters += 2;
        return new Operation(t, iters);
    }

    public boolean contains(int value) {
        Node t = root;
        while (t != null && t.value != value) {
            if (value > t.value) {
                t = t.r;
            } else if (value < t.value) {
                t = t.l;
            }
        }
        return t != null;
    }

    //тот-же contains(), но вместо true или false возвращает код-во итераций произведенных при пооске
    public int lookFor(int value){
        int iters = 0;
        Node t = root;
        while (t != null && t.value != value) {
            if (value > t.value) {
                t = t.r;
            } else if (value < t.value) {
                t = t.l;
            }
            iters++;
        }
        return iters;
    }

    public Node decreaseLevel(Node t){
        if(t.r != null){
            int shouldBe = Math.min((t.l == null ? Node.nullNode : t.l).level, t.r.level) + 1;
            if(shouldBe < t.level){
                t.level = shouldBe;
                if(shouldBe < t.r.level){
                    t.r.level = root.level;
                }
            }
        }

        return t;
    }

    public int delete(int value){
        Operation op = delete(value, root);
        root = op.node;
        return op.iterations;
    }

    public Operation delete(int value, Node t){
        int iters = 1;
        if(t == null){
            iters += 1;
            return new Operation(null, iters);
        }else if(value > t.value){
            Operation op = delete(value, t.r);
            t.r = op.node;
            iters += op.iterations;
        }else if(value < t.value){
            Operation op = delete(value, t.l);
            t.l = op.node;
            iters += op.iterations;
        }else{
            if(t.isLeaf()){
                iters += 1;
                return new Operation(null, iters);
            }else if(t.l != null){
                Operation suc = t.successor();
                Node l = suc.node;
                Operation op =  delete(l.value, t.r);
                t.r = op.node;
                t.value = l.value;
                iters += op.iterations + suc.iterations;
            }else{
                Operation pre = t.successor();
                Node l = pre.node;
                Operation op = delete(l.value, t.l);
                t.l = op.node;
                t.value = l.value;
                iters += op.iterations + pre.iterations;
            }
        }
        t = decreaseLevel(t);
        t = skew(t);
        t.r = skew(t.r);
        iters+=3;
        if(t.r != null){
            t.r.r = skew(t.r.r);
            iters+=1;
        }
        t = split(t);
        t.r = split(t.r);
        iters+=2;
        return new Operation(t, iters);
    }

    public void print(){
        print(root,0);
        System.out.println();
    }
    private void print(Node t, int level){
        for (int i = 0; i < level; i++) {
            System.out.print("-");
        }
        System.out.println(t.value +" ("+t.level+")");
        if(t.l != null){
            print(t.l, level+1);
        }
        if(t.r != null){
            print(t.r, level+1);
        }
    }

    private record Operation(Node node, int iterations){}

    private static class Node{
        Integer value;
        private Node l;
        private Node r;

        private int level;

        public Node(Integer value, int level, Node l, Node r) {
            this.value = value;
            this.l = l;
            this.r = r;
            this.level = level;
        }

        public boolean isLeaf(){
            return l == null && r == null;
        }

        public Operation successor(){
            int iters = 0;
            Node t = l;
            if(t == null) t = r;
            while (!t.isLeaf()){
                if(t.r != null){
                    t = t.r;
                }else{
                    t = t.l;
                }
                iters++;
            }
            return new Operation(t, iters);
        }

        public Operation predecessor(){
            int iters = 0;
            Node t = r;
            if(t == null) t = l;
            while (!t.isLeaf()){
                if(t.l != null){
                    t = t.l;
                }else{
                    t = t.r;
                }
                iters++;
            }
            return new Operation(t, iters);
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Node getL() {
            return l;
        }

        public void setL(Node l) {
            this.l = l;
        }

        public Node getR() {
            return r;
        }

        public void setR(Node r) {
            this.r = r;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public static Node nullNode = new Node(0, 0, null, null);
    }
}
