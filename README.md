# Meganekko CameraSample

端末のカメラの映像をVRで表示するサンプルです。

app/src/main/assetsの中にosigファイルを入れてビルドしてください。

## ポイント

1. `SceneObject.material().getSurfaceTexture()`で`SurfaceTexture`を取得できます。この`SurfaceTexture`に`Camera`の映像を送るようにします。
2. `SurfaceTexture.updateTexImage()`で画像を更新しないとOpenGLのテクスチャーが更新されません。毎フレーム`updateTexImage()`を呼び出すようにします。
3. カメラの映像を表示するオブジェクトは常に前方に置かれていて、常にこちら側に正面を向ける必要があります。これは以下のコードで実現できます。

```java
SceneObject cameraObject;

Vector3f pos = new Vector3f(0, 0, -4);
Quaternionf headRotation = getScene().getViewOrientation();
headRotation.transform(pos); // headRotationで変換されたあとの(0, 0, -4)がposに格納される
cameraObject.position(pos);
cameraObject.rotation(headRotation); // こちら側に正面を向ける
```
