class ProgressBarRotating extends Thread {
    boolean showProgress = true;
    public void run() {
        String[] anim = {
                "[*         ]",
                "[ *        ]",
                "[  *       ]",
                "[   *      ]",
                "[    *     ]",
                "[     *    ]",
                "[      *   ]",
                "[       *  ]",
                "[        * ]",
                "[         *]"
        };
        int x = 0;
        while (showProgress) {
            System.out.print("\rFinding: "+anim[(x++ % anim.length)]);
            try { Thread.sleep(200); }
            catch (Exception e) {};
        }
    }
}
