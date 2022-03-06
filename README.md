# アプリナビ Kotlin HandsOn

## 1.4　処理を記述する

今回は作成したUIの処理を記述していきます。

## View Bindingを設定する。
コードからUIにアクセスする方法として`findViewById`があります。しかしこれには以下のデメリットがあります。

- 存在しないIDを参照できる。
- キャッシュされない。呼び出すたびにViewを探索する。

そこで`View Binding`を使います。`view Binding`は各レイアウトファイルごとにバインディングクラスが生成され、バインディングクラスのインスタンスは対象のレイアウト内でIDを持つ全てのビューへの直接参照を保持しています。ですので、存在しないIDを参照することや、呼び出すたびにいちいち探索する必要がなくなります。

- build.gradle(Module)を開きます。
- 以下の内容を`android`の`{}`の内部に追加します。

```gradle
  viewBinding {
      enabled = true
  }
```

- 追加したら`Sync Now`をクリックしましょう

## 登録ボタンが押されたときの処理を追加する

- `Sync Now`が完了したら`RegisterActivity`を開き、以下の緑色のハイライトを追加しましょう
- `import`は`private lateinit ~`を先に記述することで自動で追加してくれます

```diff
   package com.example.handsonchatapp

   import androidx.appcompat.app.AppCompatActivity
   import android.os.Bundle
+  import com.example.handsonchatapp.databinding.ActivityRegisterBinding

  class RegisterActivity : AppCompatActivity() {
+
+     private lateinit var binding : ActivityRegisterBinding
+
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
-         setContentView(R.layout.activity_register)

+         binding = ActivityRegisterBinding.inflate(layoutInflater)
+         val view = binding.root
+         setContentView(view)
      }
  }
```

- さっそく処理を追加していきます。まずは、登録ボタンを押したときのイベントを書いていきます。
- 登録ボタンを押すと入力内容を保存するという処理をするのですが、それは後ほど設定します。
- 今回は登録ボタンを押すと入力内容をログで出力するという処理を追加します。

```diff
  import...

  class RegisterActivity : AppCompatActivity() {

      private lateinit var binding : ActivityRegisterBinding

+     private val TAG = "RegisterActivity"

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_register)

          binding = ActivityRegisterBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

+        binding.registerButtonRegister.setOnClickListener {
+          val email = binding.emailEdittextRegister.text.toString();
+          val password = binding.passwordEdittextRegister.text.toString();
+
+          Log.d(TAG, "Email is: ${email}")
+          Log.d(TAG, "password is: ${password}")
+       }
      }
  }
```

- もし`Unresolved reference:Log`というエラーメッセージが出力されたときはLogのところにカーソルをあわせ、`Alt`＋`Enter`で`import`しましょう。
- ここまで完了しましたら一度実行してみましょう。
- ユーザー名・メールアドレス・パスワードに適当に入力して登録ボタンを押すとログが出力されるかと思います

![session1-4-output-log](https://user-images.githubusercontent.com/57338033/156873664-5fda6438-5718-4fbe-b076-d4178cde9501.png)

- もしLogが大量にあって見ずらいということであれば検索で`Register`と入力すると見やすくなるかと思います。

- ここで今回追加したコードの解説をします。
  - Viewという言葉が出てきますが、Viewとはクラスであり今まで配置してきたテキストやボタンはViewから派生してできたサブクラスです。ですので、今後Viewという言葉がでてきたら「ボタンやテキストのことなんだな」というかんじで捉えていただければ問題ないと思います。
  - `binding.registerButtonRegister`は`activity_register`のidが`register_button_register`であるViewにアクセスしています。
  - Viewにアクセスするときは`binding.{id}`でアクセスできます。このときidは自動生成の際に[キャメルケース](https://e-words.jp/w/%E3%82%AD%E3%83%A3%E3%83%A1%E3%83%AB%E3%82%B1%E3%83%BC%E3%82%B9.html)に変更されています。
  - `setOnClickListener`はViewがタップされたときに実行される処理を登録します。`{}`で囲まれた部分の処理を実行します。

## 「すでにアカウントをお持ちですか？」を押されたときの処理を追加する
- このテキストにはタップされたときログイン画面へ遷移させる役割をもたせます。
- とりあえずさきほどのようにこのテキストに`binding`でアクセスし、タップされたときの処理を追加します。

```diff
    ...略
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
       }

+       binding.haveAccountTextRegister.setOnClickListener {
+           Log.d(TAG, "try to show login activity")
+       }
    }
    ...略
```

- 実行してログに「try to show login activity」が表示されれば大丈夫です。

## 画面遷移する
- ログイン画面を作り、ユーザー登録画面から遷移できるようにします。
- まず下図のように`RegisterActivity`の一つ上層のファイルで右クリックして、`New`→`Activity`→`Empty Activity`を選択します。

![image](https://user-images.githubusercontent.com/57338033/156875803-8bd6529e-32f6-40fc-8dac-2e88d6b4e6c0.png)

- `LoginActivity`という名前でファイルを作成します。
- すると`java/com.example.{app name}`に`LoginActivity`が、`res/layout`に`activity_login`というxmlファイルが作成されます。

![session1-4-create-new-activity-file](https://user-images.githubusercontent.com/57338033/156876004-e56282a9-afd7-4a64-b792-dcdfda3fcd5e.png)

- `RegisterActivity`に戻り、以下の内容を追加します。
- `unresolved reference:Intent`というエラーが出た際は先程と同様`Alt`+`Enster`でimportしましょう

```diff
    ...略
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
       }

        binding.haveAccountTextRegister.setOnClickListener {
            Log.d(TAG, "try to show login activity")
+            
+           val intent = Intent(this, LoginActivity::class.java)
+           startActivity(intent)
       }
    }
    ...略
```
- インテントの説明
- それでは実行してみましょう
- 「すでにアカウントをお持ちですか？」をタップしたときに以下のように画面が変われば成功です。

![session1-4-login-activity](https://user-images.githubusercontent.com/57338033/156877293-68f79318-4db2-490a-89ff-b1df3bef43aa.png)

## 戻るボタンを付ける
- ここまでで画面遷移はできました。しかし登録画面に戻ることができません。そこで戻るボタンの実装をします。
- `manifests/AndroidManifest`を開き、以下の内容を追加します。

```diff
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.handsonchatapp">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.HandsOnChatApp">
        <activity
+           android:name=".LoginActivity"
+           android:exported="false" >
+           <meta-data
+               android:name="android.support.PARENT_ACTIVITY"
+               android:value=".RegisterActivity"/>
        </activity>
        <activity
            android:name=".RegisterActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

- 追加できましたら実行しましょう。戻るボタンが`StatusBarに表示され、押すと戻ることができます。`

![session1-4-add-backButton](https://user-images.githubusercontent.com/57338033/156877655-c7c56272-1a4d-47e3-87f5-ad7adacd6abf.png)

## SelectPhotoButtonの処理を追加する
- SelectPhotoButtonが押されると端末に保存されている写真にアクセスし、選択した写真を円形の中に表示させます。
- 以下の処理を`RegisterActivityに追加します。`

```diff
    ...略
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
       }

        binding.haveAccountTextRegister.setOnClickListener {
            Log.d(TAG, "try to show login activity")
             
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
       }
       
+      binding.selectPhotoButtonRegister.setOnClickListener {
+          Log.d(tag, "Try to show photo selector")
+
+          val intent = Intent(Intent.ACTION_PICK)
+          intent.type = "image/*"
+          startActivityForResult(intent, 0)
+      }
    }
    ...略
```

- 一旦実行してみましょう
- 写真を選択を押すと写真を選ぶ画面に移動することが確認できると思います。そして適当な写真を選ぶともとの画面に戻ってくると思います。

![session1-4-select-photo-scene](https://user-images.githubusercontent.com/57338033/156878242-1eb4e126-365f-40e1-9644-9d1fd2822b43.png)

- 写真を選択することはできましたが、選択した写真を反映することができていません。ですので、追加していきます。
- `onActivityResult`という関数があります。これを`override`することで実現します。以下の内容を追加しましょう。
- `Unresolve reference`のエラーは先程と同様にimportしましょう

```diff
    ...略
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
       }

        binding.haveAccountTextRegister.setOnClickListener {
            Log.d(TAG, "try to show login activity")
             
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
       }
       
       binding.selectPhotoButtonRegister.setOnClickListener {
           Log.d(tag, "Try to show photo selector")
 
           val intent = Intent(Intent.ACTION_PICK)
           intent.type = "image/*"
           startActivityForResult(intent, 0)
       }
    }

+   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
+       super.onActivityResult(requestCode, resultCode, data)
+
+       if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
+           Log.d(tag, "Photo was selected")
+
+           val uri = data.data
+
+           val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
+           binding.circleViewRegister.setImageBitmap(bitmap)
+           binding.selectPhotoButtonRegister.alpha = 0f
+       }
+   }
    ...略
```

- ここでコードの説明をしておきます。
- 実際に実行してみましょう
- 画像を選択してユーザー登録画面に選択した画像が丸く表示されていれば大丈夫です。

![session1-4-set-image](https://user-images.githubusercontent.com/57338033/156878794-cfebf9f7-3f00-4d0e-b317-25d001a38278.png)

## Diff

<details>
<summary>前回との差分</summary>
    <a href="https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/commit/cd83163fed87fb46b11cf33cdb612d2224078250">diff</a>
</details>

## 課題
- 現状ログイン画面には何もない状態ですので、ログイン画面を以下の通りにレイアウトを作ってみましょう。merginとIdは以下に記載するのでそのように設定してください
  - メールアドレス入力フォーム
    - `merginTop`:`206dp`
    - `merginRight`:`32dp`
    - `merginLeft`:`32dp`
    - `id`:`email_edittext_login`
  - パスワード入力フォーム
    - `merginTop`:`12dp`
    - `id`:`password_edittext_login`
  - ログインボタン
    - `merginTop`:`12dp`
    - `id`:`login_button_login`
  - 登録へ戻る
    - `merginTop`:`24dp`
    - `id`:`back_to_register_text_login`

- またログインボタンが押されたとき、入力されているEmail・Passwordをログで出力してみましょう。
- 「登録に戻る」というボタンを押されたときユーザー登録画面に戻るよう実装してみましょう。以下の関数を実行するとユーザー登録の画面に戻ります。

```
  finish()
```

- ***追記***ボタンのテキストが「登録」となっておりますが、「ログイン」に変更してください。申し訳ないです🙇

![session1-4-task-login-scene](https://user-images.githubusercontent.com/57338033/156879230-9827d280-085b-4851-9ec4-6e130d781ecf.png)

[答え](https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/commit/9965485463ce648bfe46cabd5cda73dc19cfb4ad)

## Next
