/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
    }

    public void insert(int valueToInsert) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        if (valueToInsert < value) {
            if (this.left == null) {
                this.left = new TreeNode(valueToInsert);
                this.left.height = 1;
            } else {
                this.left.insert(valueToInsert);
            }

        } else {
            if (this.right == null) {
                this.right = new TreeNode(valueToInsert);
                this.right.height = 1;
            } else {
                this.right.insert(valueToInsert);
            }
        }

       int leftHeight, rightHeight;

        if (this.right == null) {
            rightHeight = 0;
        } else
            rightHeight = this.right.getHeight();

        if (this.left == null) {
            leftHeight = 0;
        } else {
            leftHeight = this.left.getHeight();
        }

        this.height = 1 + Math.max(leftHeight, rightHeight);

        int balanceFactor = leftHeight - rightHeight;

        if (balanceFactor > 1) {
            // RR
            if (valueToInsert < this.left.value) {
                this.rightRotate();

            // RL
            } else if (valueToInsert > this.left.value) {
                this.left.leftRotate();
                this.rightRotate();
            }
        }

        if (balanceFactor < -1) {
            // LL
            if (valueToInsert > this.right.value) {
               this.leftRotate();

            // LR
            } else if (valueToInsert < this.right.value){
                this.right.rightRotate();
                this.leftRotate();
            }
        }

    }

    public int getHeight() {
        return this.height;
    }

    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private void leftRotate() {

        // swap the values
        int temp = this.value;
        this.value = this.right.getValue();
        this.right.value = temp;

        // perform rotation
        TreeNode mid = this.right;
        this.right = mid.right;
        this.left = mid;
        mid.right = mid.left;
        mid.left = null;

        mid.height = 1 + Math.max(getHeight(mid.left), getHeight(mid.right));
        this.height = 1 + Math.max(getHeight(this.left), getHeight(this.right));
    }

    private void rightRotate() {

        // swap the values
        int temp = this.value;
        this.value = this.left.getValue();
        this.left.value = temp;

        // perform rotation
        TreeNode mid = this.left;
        this.left = mid.left;
        this.right = mid;
        mid.left = mid.right;
        mid.right = null;

        mid.height = 1 + Math.max(getHeight(mid.left), getHeight(mid.right));
        this.height = 1 + Math.max(getHeight(this.left), getHeight(this.right));

    }

    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);
//        c.drawText(showValue ? String.valueOf(value) : String.valueOf(value), x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
