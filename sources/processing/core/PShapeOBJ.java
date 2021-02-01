package processing.core;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PShapeOBJ extends PShape {

    protected static class OBJFace {
        int matIdx = -1;
        String name = "";
        ArrayList<Integer> normIdx = new ArrayList<>();
        ArrayList<Integer> texIdx = new ArrayList<>();
        ArrayList<Integer> vertIdx = new ArrayList<>();

        OBJFace() {
        }
    }

    protected static class OBJMaterial {
        float d;
        PVector ka;
        PVector kd;
        PImage kdMap;
        PVector ks;
        String name;
        float ns;

        OBJMaterial() {
            this("default");
        }

        OBJMaterial(String str) {
            this.name = str;
            this.ka = new PVector(0.5f, 0.5f, 0.5f);
            this.kd = new PVector(0.5f, 0.5f, 0.5f);
            this.ks = new PVector(0.5f, 0.5f, 0.5f);
            this.d = 1.0f;
            this.ns = 0.0f;
            this.kdMap = null;
        }
    }

    public PShapeOBJ(PApplet pApplet, BufferedReader bufferedReader) {
        this(pApplet, bufferedReader, "");
    }

    public PShapeOBJ(PApplet pApplet, BufferedReader bufferedReader, String str) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        parseOBJ(pApplet, str, bufferedReader, arrayList, arrayList2, arrayList3, arrayList4, arrayList5);
        this.family = 0;
        addChildren(arrayList, arrayList2, arrayList3, arrayList4, arrayList5);
    }

    public PShapeOBJ(PApplet pApplet, String str) {
        this(pApplet, pApplet.createReader(str), getBasePath(pApplet, str));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0084, code lost:
        r1 = r8.normIdx.get(r2).intValue() - 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected PShapeOBJ(processing.core.PShapeOBJ.OBJFace r8, processing.core.PShapeOBJ.OBJMaterial r9, java.util.ArrayList<processing.core.PVector> r10, java.util.ArrayList<processing.core.PVector> r11, java.util.ArrayList<processing.core.PVector> r12) {
        /*
            r7 = this;
            r7.<init>()
            r0 = 3
            r7.family = r0
            java.util.ArrayList<java.lang.Integer> r0 = r8.vertIdx
            int r0 = r0.size()
            r1 = 3
            if (r0 != r1) goto L_0x0145
            r0 = 9
            r7.kind = r0
        L_0x0013:
            r0 = 0
            r7.stroke = r0
            r0 = 1
            r7.fill = r0
            processing.core.PVector r0 = r9.kd
            int r0 = rgbaValue(r0)
            r7.fillColor = r0
            processing.core.PVector r0 = r9.ka
            int r0 = rgbaValue(r0)
            r7.ambientColor = r0
            processing.core.PVector r0 = r9.ks
            int r0 = rgbaValue(r0)
            r7.specularColor = r0
            float r0 = r9.ns
            r7.shininess = r0
            processing.core.PImage r0 = r9.kdMap
            if (r0 == 0) goto L_0x0043
            processing.core.PVector r0 = r9.kd
            float r1 = r9.d
            int r0 = rgbaValue(r0, r1)
            r7.tintColor = r0
        L_0x0043:
            java.util.ArrayList<java.lang.Integer> r0 = r8.vertIdx
            int r0 = r0.size()
            r7.vertexCount = r0
            int r0 = r7.vertexCount
            r1 = 12
            int[] r0 = new int[]{r0, r1}
            java.lang.Class r1 = java.lang.Float.TYPE
            java.lang.Object r0 = java.lang.reflect.Array.newInstance(r1, r0)
            float[][] r0 = (float[][]) r0
            r7.vertices = r0
            r0 = 0
            r2 = r0
        L_0x005f:
            java.util.ArrayList<java.lang.Integer> r0 = r8.vertIdx
            int r0 = r0.size()
            if (r2 >= r0) goto L_0x015a
            r4 = 0
            java.util.ArrayList<java.lang.Integer> r0 = r8.vertIdx
            java.lang.Object r0 = r0.get(r2)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            int r0 = r0 + -1
            java.lang.Object r0 = r10.get(r0)
            processing.core.PVector r0 = (processing.core.PVector) r0
            java.util.ArrayList<java.lang.Integer> r1 = r8.normIdx
            int r1 = r1.size()
            if (r2 >= r1) goto L_0x015b
            java.util.ArrayList<java.lang.Integer> r1 = r8.normIdx
            java.lang.Object r1 = r1.get(r2)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            int r1 = r1 + -1
            r3 = -1
            if (r3 >= r1) goto L_0x015b
            java.lang.Object r1 = r11.get(r1)
            processing.core.PVector r1 = (processing.core.PVector) r1
            r3 = r1
        L_0x009c:
            java.util.ArrayList<java.lang.Integer> r1 = r8.texIdx
            int r1 = r1.size()
            if (r2 >= r1) goto L_0x00bc
            java.util.ArrayList<java.lang.Integer> r1 = r8.texIdx
            java.lang.Object r1 = r1.get(r2)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            int r1 = r1 + -1
            r5 = -1
            if (r5 >= r1) goto L_0x00bc
            java.lang.Object r1 = r12.get(r1)
            processing.core.PVector r1 = (processing.core.PVector) r1
            r4 = r1
        L_0x00bc:
            float[][] r1 = r7.vertices
            r1 = r1[r2]
            r5 = 0
            float r6 = r0.x
            r1[r5] = r6
            float[][] r1 = r7.vertices
            r1 = r1[r2]
            r5 = 1
            float r6 = r0.y
            r1[r5] = r6
            float[][] r1 = r7.vertices
            r1 = r1[r2]
            r5 = 2
            float r0 = r0.z
            r1[r5] = r0
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 3
            processing.core.PVector r5 = r9.kd
            float r5 = r5.x
            r0[r1] = r5
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 4
            processing.core.PVector r5 = r9.kd
            float r5 = r5.y
            r0[r1] = r5
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 5
            processing.core.PVector r5 = r9.kd
            float r5 = r5.z
            r0[r1] = r5
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 6
            r5 = 1065353216(0x3f800000, float:1.0)
            r0[r1] = r5
            if (r3 == 0) goto L_0x0121
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 9
            float r5 = r3.x
            r0[r1] = r5
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 10
            float r5 = r3.y
            r0[r1] = r5
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 11
            float r3 = r3.z
            r0[r1] = r3
        L_0x0121:
            if (r4 == 0) goto L_0x0136
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 7
            float r3 = r4.x
            r0[r1] = r3
            float[][] r0 = r7.vertices
            r0 = r0[r2]
            r1 = 8
            float r3 = r4.y
            r0[r1] = r3
        L_0x0136:
            if (r9 == 0) goto L_0x0140
            processing.core.PImage r0 = r9.kdMap
            if (r0 == 0) goto L_0x0140
            processing.core.PImage r0 = r9.kdMap
            r7.image = r0
        L_0x0140:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x005f
        L_0x0145:
            java.util.ArrayList<java.lang.Integer> r0 = r8.vertIdx
            int r0 = r0.size()
            r1 = 4
            if (r0 != r1) goto L_0x0154
            r0 = 17
            r7.kind = r0
            goto L_0x0013
        L_0x0154:
            r0 = 20
            r7.kind = r0
            goto L_0x0013
        L_0x015a:
            return
        L_0x015b:
            r3 = r4
            goto L_0x009c
        */
        throw new UnsupportedOperationException("Method not decompiled: processing.core.PShapeOBJ.<init>(processing.core.PShapeOBJ$OBJFace, processing.core.PShapeOBJ$OBJMaterial, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList):void");
    }

    protected static OBJMaterial addMaterial(String str, ArrayList<OBJMaterial> arrayList, Map<String, Integer> map) {
        OBJMaterial oBJMaterial = new OBJMaterial(str);
        map.put(str, Integer.valueOf(arrayList.size()));
        arrayList.add(oBJMaterial);
        return oBJMaterial;
    }

    protected static String getBasePath(PApplet pApplet, String str) {
        File file = new File(pApplet.dataPath(str));
        if (!file.exists()) {
            file = pApplet.sketchFile(str);
        }
        String absolutePath = file.getAbsolutePath();
        return absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    }

    protected static void parseMTL(PApplet pApplet, String str, String str2, BufferedReader bufferedReader, ArrayList<OBJMaterial> arrayList, Map<String, Integer> map) {
        OBJMaterial oBJMaterial = null;
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String[] split = readLine.trim().split("\\s+");
                    if (split.length > 0) {
                        if (split[0].equals("newmtl")) {
                            oBJMaterial = addMaterial(split[1], arrayList, map);
                        } else {
                            if (oBJMaterial == null) {
                                oBJMaterial = addMaterial("material" + arrayList.size(), arrayList, map);
                            }
                            if (split[0].equals("map_Kd") && split.length > 1) {
                                String str3 = split[1];
                                if (str3.indexOf(File.separator) == -1 && !str2.equals("")) {
                                    str3 = str2 + File.separator + str3;
                                }
                                if (new File(pApplet.dataPath(str3)).exists()) {
                                    oBJMaterial.kdMap = pApplet.loadImage(str3);
                                } else {
                                    System.err.println("The texture map \"" + str3 + "\" in the materials definition file \"" + str + "\" is missing or inaccessible, make sure the URL is valid or that the file has been added to your sketch and is readable.");
                                }
                            } else if (split[0].equals("Ka") && split.length > 3) {
                                oBJMaterial.ka.x = Float.valueOf(split[1]).floatValue();
                                oBJMaterial.ka.y = Float.valueOf(split[2]).floatValue();
                                oBJMaterial.ka.z = Float.valueOf(split[3]).floatValue();
                            } else if (split[0].equals("Kd") && split.length > 3) {
                                oBJMaterial.kd.x = Float.valueOf(split[1]).floatValue();
                                oBJMaterial.kd.y = Float.valueOf(split[2]).floatValue();
                                oBJMaterial.kd.z = Float.valueOf(split[3]).floatValue();
                            } else if (split[0].equals("Ks") && split.length > 3) {
                                oBJMaterial.ks.x = Float.valueOf(split[1]).floatValue();
                                oBJMaterial.ks.y = Float.valueOf(split[2]).floatValue();
                                oBJMaterial.ks.z = Float.valueOf(split[3]).floatValue();
                            } else if ((split[0].equals("d") || split[0].equals("Tr")) && split.length > 1) {
                                oBJMaterial.d = Float.valueOf(split[1]).floatValue();
                            } else if (split[0].equals("Ns") && split.length > 1) {
                                oBJMaterial.ns = Float.valueOf(split[1]).floatValue();
                            }
                        }
                    }
                } else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    protected static void parseOBJ(PApplet pApplet, String str, BufferedReader bufferedReader, ArrayList<OBJFace> arrayList, ArrayList<OBJMaterial> arrayList2, ArrayList<PVector> arrayList3, ArrayList<PVector> arrayList4, ArrayList<PVector> arrayList5) {
        String str2;
        boolean z;
        boolean z2;
        boolean z3;
        int i;
        HashMap hashMap = new HashMap();
        int i2 = -1;
        boolean z4 = false;
        String str3 = "object";
        boolean z5 = false;
        boolean z6 = false;
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String trim = readLine.trim();
                if (!trim.equals("") && trim.indexOf(35) != 0) {
                    while (trim.contains("\\")) {
                        trim = trim.split("\\\\")[0];
                        String readLine2 = bufferedReader.readLine();
                        if (readLine2 != null) {
                            trim = trim + readLine2;
                        }
                    }
                    String[] split = trim.split("\\s+");
                    if (split.length > 0) {
                        if (split[0].equals("v")) {
                            arrayList3.add(new PVector(Float.valueOf(split[1]).floatValue(), Float.valueOf(split[2]).floatValue(), Float.valueOf(split[3]).floatValue()));
                            z = z4;
                            z2 = z5;
                            z3 = true;
                            i = i2;
                            str2 = str3;
                        } else if (split[0].equals("vn")) {
                            arrayList4.add(new PVector(Float.valueOf(split[1]).floatValue(), Float.valueOf(split[2]).floatValue(), Float.valueOf(split[3]).floatValue()));
                            z = z4;
                            z2 = true;
                            z3 = z6;
                            i = i2;
                            str2 = str3;
                        } else if (split[0].equals("vt")) {
                            arrayList5.add(new PVector(Float.valueOf(split[1]).floatValue(), 1.0f - Float.valueOf(split[2]).floatValue()));
                            z = true;
                            z2 = z5;
                            z3 = z6;
                            i = i2;
                            str2 = str3;
                        } else if (split[0].equals("o")) {
                            str2 = str3;
                            z = z4;
                            z2 = z5;
                            z3 = z6;
                            i = i2;
                        } else if (split[0].equals("mtllib")) {
                            if (split[1] != null) {
                                String str4 = split[1];
                                if (str4.indexOf(File.separator) == -1 && !str.equals("")) {
                                    str4 = str + File.separator + str4;
                                }
                                BufferedReader createReader = pApplet.createReader(str4);
                                if (createReader != null) {
                                    parseMTL(pApplet, str4, str, createReader, arrayList2, hashMap);
                                    createReader.close();
                                }
                                str2 = str3;
                                z = z4;
                                z2 = z5;
                                z3 = z6;
                                i = i2;
                            }
                        } else if (split[0].equals("g")) {
                            str2 = 1 < split.length ? split[1] : "";
                            z = z4;
                            z2 = z5;
                            z3 = z6;
                            i = i2;
                        } else if (split[0].equals("usemtl")) {
                            if (split[1] != null) {
                                String str5 = split[1];
                                z = z4;
                                z2 = z5;
                                z3 = z6;
                                i = hashMap.containsKey(str5) ? ((Integer) hashMap.get(str5)).intValue() : -1;
                                str2 = str3;
                            }
                        } else if (split[0].equals("f")) {
                            OBJFace oBJFace = new OBJFace();
                            oBJFace.matIdx = i2;
                            oBJFace.name = str3;
                            for (int i3 = 1; i3 < split.length; i3++) {
                                String str6 = split[i3];
                                if (str6.indexOf("/") > 0) {
                                    String[] split2 = str6.split("/");
                                    if (split2.length > 2) {
                                        if (split2[0].length() > 0 && z6) {
                                            oBJFace.vertIdx.add(Integer.valueOf(split2[0]));
                                        }
                                        if (split2[1].length() > 0 && z4) {
                                            oBJFace.texIdx.add(Integer.valueOf(split2[1]));
                                        }
                                        if (split2[2].length() > 0 && z5) {
                                            oBJFace.normIdx.add(Integer.valueOf(split2[2]));
                                        }
                                    } else if (split2.length > 1) {
                                        if (split2[0].length() > 0 && z6) {
                                            oBJFace.vertIdx.add(Integer.valueOf(split2[0]));
                                        }
                                        if (split2[1].length() > 0) {
                                            if (z4) {
                                                oBJFace.texIdx.add(Integer.valueOf(split2[1]));
                                            } else if (z5) {
                                                oBJFace.normIdx.add(Integer.valueOf(split2[1]));
                                            }
                                        }
                                    } else if (split2.length > 0 && split2[0].length() > 0 && z6) {
                                        oBJFace.vertIdx.add(Integer.valueOf(split2[0]));
                                    }
                                } else if (str6.length() > 0 && z6) {
                                    oBJFace.vertIdx.add(Integer.valueOf(str6));
                                }
                            }
                            arrayList.add(oBJFace);
                        }
                        str3 = str2;
                        z4 = z;
                        z5 = z2;
                        z6 = z3;
                        i2 = i;
                    }
                    str2 = str3;
                    z = z4;
                    z2 = z5;
                    z3 = z6;
                    i = i2;
                    str3 = str2;
                    z4 = z;
                    z5 = z2;
                    z6 = z3;
                    i2 = i;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (arrayList2.size() == 0) {
            arrayList2.add(new OBJMaterial());
        }
    }

    protected static int rgbaValue(PVector pVector) {
        return -16777216 | (((int) (pVector.x * 255.0f)) << 16) | (((int) (pVector.y * 255.0f)) << 8) | ((int) (pVector.z * 255.0f));
    }

    protected static int rgbaValue(PVector pVector, float f) {
        return (((int) (f * 255.0f)) << 24) | (((int) (pVector.x * 255.0f)) << 16) | (((int) (pVector.y * 255.0f)) << 8) | ((int) (pVector.z * 255.0f));
    }

    /* access modifiers changed from: protected */
    public void addChildren(ArrayList<OBJFace> arrayList, ArrayList<OBJMaterial> arrayList2, ArrayList<PVector> arrayList3, ArrayList<PVector> arrayList4, ArrayList<PVector> arrayList5) {
        int i;
        OBJMaterial oBJMaterial = null;
        int i2 = 0;
        int i3 = -1;
        while (i2 < arrayList.size()) {
            OBJFace oBJFace = arrayList.get(i2);
            if (i3 != oBJFace.matIdx || oBJFace.matIdx == -1) {
                int max = PApplet.max(0, oBJFace.matIdx);
                i = max;
                oBJMaterial = arrayList2.get(max);
            } else {
                i = i3;
            }
            addChild(new PShapeOBJ(oBJFace, oBJMaterial, arrayList3, arrayList4, arrayList5));
            i2++;
            i3 = i;
        }
    }
}
