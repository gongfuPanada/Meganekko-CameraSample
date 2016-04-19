package com.eje_c.camerasample;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.eje_c.meganekko.Meganekko;
import com.eje_c.meganekko.MeganekkoApp;
import com.eje_c.meganekko.SceneObject;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.IOException;

public class App extends MeganekkoApp {
    private Camera camera;
    private SceneObject cameraObject;
    private SurfaceTexture cameraTargetTexture;

    public App(Meganekko meganekko) {
        super(meganekko);

        // シーンを設定
        setSceneFromXML(R.xml.scene);

        // カメラの映像を描画するシーンオブジェクトを取得
        cameraObject = getScene().findObjectById(R.id.camera);
        cameraTargetTexture = cameraObject.material().getSurfaceTexture();

        // カメラを初期化する
        try {
            openCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

        // カメラの映像をシーンオブジェクトのテクスチャに反映する
        cameraTargetTexture.updateTexImage();

        // 視線の先にシーンオブジェクトを配置する
        Vector3f pos = new Vector3f(0, 0, -4);
        Quaternionf headRotation = getScene().getViewOrientation();
        headRotation.transform(pos);
        cameraObject.position(pos);
        cameraObject.rotation(headRotation);

        super.update();
    }

    /**
     * Gear VRを外してアプリがスリープ状態になったら呼ばれる
     */
    @Override
    public void onPause() {
        super.onPause();

        // カメラを開放する
        releaseCamera();
    }

    /**
     * Gear VRを外してアプリがスリープ状態になった後再度Gear VRが装着された時に呼ばれる
     */
    @Override
    public void onResume() {
        super.onResume();
        try {
            openCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() throws IOException {
        synchronized (App.class) {
            if (camera == null) {
                camera = Camera.open();
                camera.setPreviewTexture(cameraTargetTexture);
                camera.startPreview();
            }
        }
    }

    private void releaseCamera() {
        synchronized (App.class) {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }
    }
}
