# アプリナビ Kotlin HandsOn

## 2.2　アプリに認証機能とログイン機能を追加する

今回はアプリに認証・ログイン機能を追加します。登録ボタンを押すと入力内容をFirebaseに保存し、ユーザー登録できるようにします。また、ログインボタンを押すと入力内容が正しければログインできるよう実装していきます。

## 認証機能の追加

- [Firebase]()のプロジェクトにアクセスし左にバーにある`Authentication`を選択します。
- 「始める」を選択します。
- 上に並んでいる項目のうち`Sign-in method`を開きます。ログインプロバイダとして`メール/パスワード`を選択します。
- `メール/パスワード`を有効にして保存します。

![session2 1-enable-signin-and-password](https://user-images.githubusercontent.com/57338033/156908845-a376c092-0285-4cea-9f87-8d637dd7626d.png)

- つづいてAndroidStudioを開きます。
- `build.gradle(Module)`を開き、以下の内容を`dependencies`の中に追加します。追加できましたら`Sync Now`をしましょう([ドキュメント](https://firebase.google.com/docs/auth/android/start?hl=ja))

```
  implementation 'com.google.firebase:firebase-auth-ktx'
```

- `RegisterActivity`を開き、以下の緑色のハイライトの内容を追加しましょう

```diff
  package com.example.handsonchatapp

...
+ import com.google.firebase.auth.FirebaseAuth

  class RegisterActivity : AppCompatActivity() {
  略

      override fun onCreate(savedInstanceState: Bundle?) {
      略

          binding.registerButtonRegister.setOnClickListener {
                val email = binding.emailEdittextRegister.text.toString();
                val password = binding.passwordEdittextRegister.text.toString();

                Log.d(TAG, "Email is: ${email}")
                Log.d(TAG, "password is: ${password}")

+             FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
+                 .addOnCompleteListener{
+                     if (it.isCanceled){
+                         Log.d(TAG, "Canceled")
+                     }
+                     if (!it.isSuccessful) {
+                         Log.d(TAG, "Failed to create user ${it.exception}")
+                         return@addOnCompleteListener
+                     }
+
+                     Log.d(TAG, "Successfully created user with uid: ${it.result.user?.uid}")
+                 }
         }
```

- 追加できましたら実行しましょう。ログを確認し、`Successfully created user wwith uid: ~`と出力されましたら成功です。
- Firebaseの`Authentication`の`users`を確認すると追加されているかと思います。

![session2 1-successfully-regist-user](https://user-images.githubusercontent.com/57338033/156910931-e5d80691-88c0-4881-a209-5d59f67ec7ad.png)

![session2 1-firebase-user-view](https://user-images.githubusercontent.com/57338033/156910954-eed461ad-90d3-4fab-8316-5c204119e7a1.png)

<details>
<summary>エラーが出てしまったとき</summary>
  
- `sign In method`で`メール/パスワード`が有効になっているか確認しましょう
- 端末からこのプロジェクトのアプリケーションをアンインストールして再度実行してみましょう。
  
</details>

- ここで先程のコードを少し修正します。
  - `registerButtonRegister`の`setOnClickListener`を`performRegister`という名前でメソッド抽出します。
  - 次に`registerButtonRegister`を以下のように書き換えます。

```kotlin

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
            }
            .addOnFailureListener{
                Log.d(TAG, "failed to create user message ${it.message}")
                Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
            }
    }
```

- ここまでのRegisterActivityの全体を以下に記載します。

<details>

<summary>RegisterActivity</summary>
  
  ``` kotlin
  package com.example.handsonchatapp

  import android.app.Activity
  import android.content.Intent
  import androidx.appcompat.app.AppCompatActivity
  import android.os.Bundle
  import android.provider.MediaStore
  import android.util.Log
  import android.widget.Toast
  import com.example.handsonchatapp.databinding.ActivityRegisterBinding
  import com.google.firebase.auth.FirebaseAuth

  class RegisterActivity : AppCompatActivity() {

      private lateinit var binding : ActivityRegisterBinding

      private val TAG = "RegisterActivity"

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          binding = ActivityRegisterBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

          binding.registerButtonRegister.setOnClickListener {
                val email = binding.emailEdittextRegister.text.toString();
                val password = binding.passwordEdittextRegister.text.toString();

                Log.d(TAG, "Email is: ${email}")
                Log.d(TAG, "password is: ${password}")

              FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                  .addOnCompleteListener{
                      if (it.isCanceled){
                          Log.d(TAG, "Canceled")
                      }
                      if (!it.isSuccessful) {
                          Log.d(TAG, "Failed to create user ${it.exception}")
                          return@addOnCompleteListener
                      }

                      Log.d(TAG, "Successfully created user with uid: ${it.result.user?.uid}")
                  }
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
              }
              .addOnFailureListener{
                  //emailのformatが違ったら実行
                  Log.d(TAG, "failed to create user message ${it.message}")
                  Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show()
              }
      }

      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)

          if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
              Log.d(TAG, "Photo was selected")

              val uri = data.data

              //APIレベルによってbitmapの取得方法の推奨が違う
              val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
              binding.circleViewRegister.setImageBitmap(bitmap)
              binding.selectPhotoButtonRegister.alpha = 0f
          }
      }
  }
  ```

</details>

## ログイン機能の追加

- `LoginActivity`を開きます。
- 現状の`LoginActivity`は以下のようになっているかと思います。

```kotlin
  package com.example.handsonchatapp

  import androidx.appcompat.app.AppCompatActivity
  import android.os.Bundle
  import android.util.Log
  import com.example.handsonchatapp.databinding.ActivityLoginBinding

  class LoginActivity : AppCompatActivity() {

      private lateinit var binding: ActivityLoginBinding

      private val TAG = "LoginActivity"

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          binding = ActivityLoginBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

          binding.loginButtonLogin.setOnClickListener {
              val email = binding.emailEdittextLogin.text.toString()
              val password = binding.passwordEdittextLogin.text.toString()

              Log.d(TAG, "email : ${email}, password:${password}")
          }

          binding.backToRegisterTextLogin.setOnClickListener {
              finish()
          }
      }
  }
```

- 以下の緑色のハイライトを追加します。

```diff
  package com.example.handsonchatapp

  import androidx.appcompat.app.AppCompatActivity
  import android.os.Bundle
  import android.util.Log
+ import android.widget.Toast
  import com.example.handsonchatapp.databinding.ActivityLoginBinding
+ import com.google.firebase.auth.FirebaseAuth

  class LoginActivity : AppCompatActivity() {

      private lateinit var binding: ActivityLoginBinding

      private val TAG = "LoginActivity"

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          binding = ActivityLoginBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

          binding.loginButtonLogin.setOnClickListener {
              val email = binding.emailEdittextLogin.text.toString()
              val password = binding.passwordEdittextLogin.text.toString()

+             if (email.isEmpty() || password.isEmpty()) {
+                 Toast.makeText(this, "Please enter text in email or password", Toast.LENGTH_SHORT).show()
+                 return@setOnClickListener
+             }

              Log.d(TAG, "email : ${email}, password:${password}")
+
+             FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
+                 .addOnCompleteListener {
+                     if (!it.isSuccessful) {
+                         Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show()
+                         return@addOnCompleteListener
+                     }
+
+                     Log.d(TAG, "Successful Login")
+                 }
+                 .addOnFailureListener {
+                     Toast.makeText(this, "Failed to Login", Toast.LENGTH_SHORT).show()
+                 }
+         }

          binding.backToRegisterTextLogin.setOnClickListener {
              finish()
          }
      }
  }
```

- 入力できましたら実行しましょう。
- LOGの検索で`Login`と入力後Formに先程ユーザー登録した情報を入力し、ログインボタンを押すと'success login'と出力されれば問題ないです。
<br>
このSessionではFirebaseを導入し、ユーザー登録機能・ログイン機能を追加しました。今後ユーザー登録後またはログイン後にユーザー一覧画面へ遷移するよう実装します。<br>
次のセクションではユーザー一覧画面を作成していきます。

## Diff

<details>
  <summary>前回との差分</summary>
    <a href="https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/commit/c37b5b1d8089d65641a1b6aada2b242e77842853">diff</a>
</details>

## Next
