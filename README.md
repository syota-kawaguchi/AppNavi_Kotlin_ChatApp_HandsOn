# アプリナビ Kotlin HandsOn

## 3.3　データベースへ登録・MessageActivityのリストに反映
今回はデータベースにユーザー情報を登録し、MessageActivityのリストに反映させます。

## Realtime Databaseの準備
- Firebaseを開き、`Realtime Database`を選択します。
- `データベースを作成`を選択します。ロケーションはどこでもいいです。次へを選択します。
- `テストモードで開始する`にチェックを入れ有効にします。
- Firebase側は完了です。

![session3 3-done-set-up-realtime-database](https://user-images.githubusercontent.com/57338033/156995615-ee744de0-7584-4b0b-9466-e18bacc2ac06.png)

- 次にプロジェクト側の設定をします。
- `build.gradle(Module)`を開き、`dependencies`に以下の行を追加し、`Sync Now`をクリックしましょう。

```
  implementation 'com.google.firebase:firebase-database-ktx'
```

## Firebase Storageの準備
- Firebaseを開き、`Storage`を選択します。
- `始める`を選択します。
- `テストモードで開始する`にチェックをいれ次へを選択します。
- ロケーションはそのままでいいです。完了をクリックしましょう。

![session3 3-done-set-up-storage](https://user-images.githubusercontent.com/57338033/156995649-218644e1-4d24-4032-8e5f-d840fe952c71.png)

- 次にプロジェクト側を編集します。
- `build.gradle(Module)`を開き、`dependencies`に以下の行を追加し、`Sync Now`をクリックしましょう。

```
  implementation 'com.google.firebase:firebase-storage-ktx'
```

## ユーザー情報の保存
- ユーザー登録時にユーザー情報を保存するようにします。
- 保存するにあたって`User`クラスを定義します。
- `New` → `Kotlin Java/Class`を`User`という名前で作成します。
- `User`ファイルを以下のようにします。

```kotlin
  package com.example.handsonchatapp
    class User(val uid: String, val username: String, val profileImageUrl: String){
      constructor() : this("", "", "")
  }
```

- 続いて`RegisterActivity`を開き、以下のように編集します。赤のハイライトは削除で、緑のハイライトは追加してください。

```diff
  package com.example.handsonchatapp
  import android.app.Activity
  import android.content.Intent
+ import android.net.Uri
  import androidx.appcompat.app.AppCompatActivity
  import android.os.Bundle
  import android.provider.MediaStore
  import android.util.Log
  import android.widget.Toast
  import com.example.handsonchatapp.databinding.ActivityRegisterBinding
  import com.google.firebase.auth.FirebaseAuth
+ import com.google.firebase.database.FirebaseDatabase
+ import com.google.firebase.storage.FirebaseStorage
+ import java.util.*

  class RegisterActivity : AppCompatActivity() {
        private lateinit var binding : ActivityRegisterBinding
        
        private val TAG = "RegisterActivity"
+
+       var selectPhotoUri: Uri? = null

        override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          
          binding = ActivityRegisterBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)
          
          binding.registerButtonRegister.setOnClickListener {
              performRegister()
          }
          
          binding.haveAccountTextRegister.setOnClickListener {
              val intent = Intent(this, LoginActivity::class.java)
              startActivity(intent)
              Log.d(TAG, "try to show login activity")
          }
          
          binding.selectPhotoButtonRegister.setOnClickListener {
              Log.d(TAG, "Try to show photo selector")
              val intent = Intent(Intent.ACTION_PICK)
              intent.type = "image/*"
              startActivityForResult(intent, 0)
          }
      }
      
      private fun performRegister() {
          val email = binding.emailEdittextRegister.text.toString();
          val password = binding.passwordEdittextRegister.text.toString();
          
          if (email.isEmpty() || password.isEmpty()) {
              Toast.makeText(this, "Please enter text in email or password", Toast.LENGTH_SHORT).show()
              return
          }
          
          Log.d(TAG, "Email is: ${email}")
          Log.d(TAG, "password is: ${password}")
          FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
              .addOnCompleteListener{
                  if (!it.isSuccessful) {
                      Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
                      return@addOnCompleteListener
                  }
                  //else if successful
                  Log.d(TAG, "Successfully created user with uid: ${it.result.user?.uid}")
-                val intent = Intent(this, MessageActivity::class.java)
-                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
-                startActivity(intent)
+
+                 uploadImageToFirebaseStorage()
              }
              .addOnFailureListener{
                  //emailのformatが違ったら実行
                  Log.d(TAG, "failed to create user message ${it.message}")
                  Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
              }
      }
      
+     private fun uploadImageToFirebaseStorage() {
+         if (selectPhotoUri == null) {
+             Toast.makeText(this, "please select an Image", Toast.LENGTH_SHORT).show()
+             return
+         }
+
+         val filename = UUID.randomUUID().toString()
+         val ref = FirebaseStorage.getInstance().getReference("image/$filename")
+
+         ref.putFile(selectPhotoUri!!)
+             .addOnSuccessListener {
+                 Log.d(TAG, "Successfully uploaded image:${it.metadata?.path}")
+
+                 ref.downloadUrl.addOnSuccessListener {
+                     Log.d(TAG, "File location :$it")
+
+                     saveUserToFirebaseDatabase(it.toString())
+                 }
+            }
+            .addOnFailureListener {}
+
+         val intent = Intent(this, MessageActivity::class.java)
+         intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
+         startActivity(intent)
+     }
+
+     private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
+         val uid = FirebaseAuth.getInstance().uid ?: ""
+         val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
+
+         val user = User(uid, binding.usernameEdittextRegister.text.toString(), profileImageUrl)
+
+         ref.setValue(user)
+             .addOnSuccessListener {
+                Log.d(TAG, "saved the user to Firebase Database")
+
+                 val intent = Intent(this, MessageActivity::class.java)
+                 intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
+                 startActivity(intent)
+             }
+     }
+
      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)
          
          if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
              Log.d(TAG, "Photo was selected")
-             val uri = data.data
+             selectPhotoUri = data.data
-             val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
+             val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoUri)
              binding.circleViewRegister.setImageBitmap(bitmap)
              binding.selectPhotoButtonRegister.alpha = 0f
          }
      }
  }
 ```
  
- 追加できましたら一度FirebaseのAuthenticationを開き、今まで登録してきたユーザーをすべて削除します。
- 削除できましたら、アプリを実行してみましょう。ユーザー登録画面からユーザーを登録してみましょう。
- 下図のようにログが出力され、画面が遷移されることを確認できましたでしょうか？

![session3 3-done-user-register](https://user-images.githubusercontent.com/57338033/157011261-2c28ce01-a172-4c85-acaa-49acd230d54d.png)

- 確認できましたらFirebaseを見てみましょう。下図のように保存されていればOKです。

![session3 3-result-realtime-database](https://user-images.githubusercontent.com/57338033/157013724-76ff1518-d6af-4e5d-b1a0-310b8b3e1a02.png)

![session3 3-result-storage](https://user-images.githubusercontent.com/57338033/157013738-86c292f9-4a77-4ee3-adb6-2b5e42076af5.png)

今回はここまでです。次はチャット画面・機能の実装をしていきます。

## Diff

<details>

<summary>前回との差分</summary>

  [diff](https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/commit/134df6d7545bbbeea33f098af15348100bea0620)
  
</details>
