# アプリナビ Kotlin HandsOn

## 5.2 手直し
今回は以下の画面のようにメッセージ画面にユーザーごとに最新のメッセージを表示させます。

## メッセージ送信時に入力をクリア・下までスクロール
- 送信ボタンを押すとメッセージは送信されますが入力がクリアされず、次にメッセージを入力する前に消さないといけません。
- またメッセージが少ないとあまり気になりませんが、送信しても最新のメッセージまでスクロールされません。
- ということでこれらを実装します。`ChatLogActivity`に以下の処理を追加しましょう。

```diff
  package com.example.handsonchatapp

  import ...

  class ChatLogActivity : AppCompatActivity() {

      private val TAG = "ChatLogActivity"

      private lateinit var binding : ActivityChatLogBinding

      private var recyclerView : RecyclerView? = null

      var toUser : User? = null

      val chatLogs = mutableListOf<ChatLogItem>()

      override fun onCreate(savedInstanceState: Bundle?) {
          ...
      }

      private fun refreshRecyclerView(list : List<ChatLogItem>) {
          ... 
      }

      private fun performSendMessage() {
          val user = intent.getParcelableExtra<User>(USER_KEY)
          val text = binding.edittextChatlog.text.toString()
          val fromId = FirebaseAuth.getInstance().uid
          val toId = user?.uid

          if (fromId == null || toId == null || text == "") return

          val fromRef = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
          val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

          val chatMessage = ChatMessage(fromRef.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
          fromRef.setValue(chatMessage)
              .addOnSuccessListener {
                  Log.d(TAG, "Saved our chat message: ${fromRef.key}")
+                 binding.edittextChatlog.text.clear()
+                 binding.recyclerviewChatlog.scrollToPosition(chatLogs.count() - 1)
              }
          toRef.setValue(chatMessage)
              .addOnSuccessListener {
                  Log.d(TAG, "Saved our chat message: ${toRef.key}")
              }

          val latestFromRef = FirebaseDatabase.getInstance().getReference("latest-messages/$fromId/$toId")
          latestFromRef.setValue(chatMessage)

          val latestToRef = FirebaseDatabase.getInstance().getReference("latest-messages/$toId/$fromId")
          latestToRef.setValue(chatMessage)
      }

      private fun listenForMessage() {
          ...
      }
  }
```

- 追加できましたら実行してみましょう。メッセージをいくつか送信して送信時に入力がクリアされ、下までスクロールされればOKです。

## アイコンにフレームを追加する。
- 現状アイコンの背景が白だと後ろと同化してしまうので、あまり目立たない程度に枠を設定します。
- `message_row.xml`を開き、以下の行を追加します。

```diff
  <?xml version="1.0" encoding="utf-8"?>
  <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/message_textview_message"
    android:layout_width="match_parent"
    android:layout_height="100dp">

    <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/userimage_imageview_message"
        android:layout_width="64dp"
        android:layout_height="64dp"
        android:layout_marginStart="16dp"
+       app:civ_border_width="1dp"
+       app:civ_border_color="@color/chat_to_row_background"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:srcCompat="@tools:sample/avatars" />

    <TextView
        android:id="@+id/username_textview_message"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginBottom="8dp"
        android:text="username"
        android:textSize="16sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toTopOf="@+id/latestmessage_textview_message"
        app:layout_constraintStart_toEndOf="@+id/userimage_imageview_message"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_chainStyle="packed" />

    <TextView
        android:id="@+id/latestmessage_textview_message"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginEnd="8dp" android:text="latest message"
        android:textSize="16sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toEndOf="@+id/userimage_imageview_message"
        app:layout_constraintTop_toBottomOf="@+id/username_textview_message" />
  </androidx.constraintlayout.widget.ConstraintLayout>
```

- 同様に`chat_from_row.xml`を以下の容易編集します。

```diff
  <?xml version="1.0" encoding="utf-8"?>
  <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/imageView_chat_log"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="8dp"
+       app:civ_border_width="1dp"
+       app:civ_border_color="@color/chat_to_row_background"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:srcCompat="@tools:sample/avatars" />

    <TextView
        android:id="@+id/textview_chat_log"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="8dp"
        android:background="@drawable/chat_log_from_background"
        android:maxWidth="240dp"
        android:padding="16dp"
        android:text="This is my message that will wrap into multiple lines and keep on going"
        android:textColor="@color/white"
        app:layout_constraintEnd_toStartOf="@+id/imageView_chat_log"
        app:layout_constraintTop_toTopOf="@+id/imageView_chat_log" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

- `chat_to_row.xml`を以下のように編集します。

```diff
  <?xml version="1.0" encoding="utf-8"?>
  <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/imageView_chat_log"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_marginTop="8dp"
        android:layout_marginStart="8dp"
+       app:civ_border_width="1dp"
+       app:civ_border_color="@color/chat_to_row_background"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:srcCompat="@tools:sample/avatars" />

    <TextView
        android:id="@+id/textview_chat_log"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:background="@drawable/chat_log_from_background"
        android:maxWidth="240dp"
        android:padding="16dp"
        android:text="This is my message that will wrap into multiple lines and keep on going"
        android:textColor="@color/white"
        app:layout_constraintStart_toEndOf="@+id/imageView_chat_log"
        app:layout_constraintTop_toTopOf="@+id/imageView_chat_log" />
  </androidx.constraintlayout.widget.ConstraintLayout>
```

- ついでにMessage画面のアイテム間にDividerを設定します。下図のように線を引きます。

![session5 2-add-frame-message-activity](https://user-images.githubusercontent.com/57338033/157472431-d336005d-a1df-48ad-a342-c22f04f964e8.png)

- `MessageActivity`に以下の処理を追加しましょう

```diff
  package com.example.handsonchatapp

  import ...

  val USER_KEY = "USER_KEY"

  suspend fun DatabaseReference.awaitGet(): DataSnapshot {
      ...
  }

  class MessageActivity : AppCompatActivity() {

      companion object {
          var currentUser: User? = null
      }

      private val TAG = "Message Activity"

      private lateinit var binding : ActivityMessageBinding

      var recyclerView: RecyclerView? = null

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          binding = ActivityMessageBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

          recyclerView = binding.recyclerViewMessage

+         val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager(this).orientation)
+         recyclerView?.addItemDecoration(dividerItemDecoration)

          fetchCurrentUser()

          listenForLatestMessage()
      }

      private fun fetchCurrentUser() {
          ...
      }

      private fun listenForLatestMessage() {
          ...
      }

      private fun refreshRecyclerView(messageItems : List<MessageItem>) {
          ...
      }

      override fun onOptionsItemSelected(item: MenuItem): Boolean {
          ...
      }

      override fun onCreateOptionsMenu(menu: Menu?): Boolean {
          ...
      }
  }
```

- ここまで追加できましたら実行してみましょう。以下のようにフレームやDividerが表示されて入ればOKです。(若干見にくいですが...)

![session5 2-add-frame-message-activity](https://user-images.githubusercontent.com/57338033/157473072-0502b5ed-c151-45fa-822e-c65fde5ff8e2.png)
![session5 2-add-frame-chat-log-activity](https://user-images.githubusercontent.com/57338033/157473208-c0b9c9d6-41cf-4807-a32a-716cf8274950.png)

## アクションバーのタイトルを変える

- 最後にアクションバーのタイトルを変えましょう。
- `RegisterActivity`を開き、以下の処理を追加します。

```diff
  package com.example.handsonchatapp

  import ...

  class RegisterActivity : AppCompatActivity() {
  
      ...

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          binding = ActivityRegisterBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

+         supportActionBar?.title = "登録"

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
      
      ...
```

- `LoginActivity`を開き、以下のように編集します・

```
  package com.example.handsonchatapp

  import ...
  
  class LoginActivity : AppCompatActivity() {

      private lateinit var binding: ActivityLoginBinding

      private val TAG = "LoginActivity"

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          binding = ActivityLoginBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

+         supportActionBar?.title = "ログイン"

          ...
      }
  }

```

- `MessageActivity`を開き、以下のように編集します。

```diff
  package com.example.handsonchatapp

  import ...

  val USER_KEY = "USER_KEY"

  suspend fun DatabaseReference.awaitGet(): DataSnapshot {
      ...
  }

  class MessageActivity : AppCompatActivity() {

      companion object {
          var currentUser: User? = null
      }

      private val TAG = "Message Activity"

      private lateinit var binding : ActivityMessageBinding

      var recyclerView: RecyclerView? = null

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          binding = ActivityMessageBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

          recyclerView = binding.recyclerViewMessage

+         supportActionBar?.title = "メッセージ"

          val dividerItemDecoration =
              DividerItemDecoration(this, LinearLayoutManager(this).orientation)
          recyclerView?.addItemDecoration(dividerItemDecoration)

          fetchCurrentUser()

          listenForLatestMessage()
      }

      ...
```

- 追加できましたら実行してみましょう。アクションバーにそれぞれ指定した文字が表示されていればOKです 

![session5 2-edit-actionbar-login-activity](https://user-images.githubusercontent.com/57338033/157477270-6b07fdc6-435c-4e30-a1d7-88cb3236d120.png)

![session5 2-edit-actionbar-message-activity](https://user-images.githubusercontent.com/57338033/157477278-5b06c779-9675-4273-ab73-c4942115264d.png)

![session5 2-edit-actionbar-register-activity](https://user-images.githubusercontent.com/57338033/157477280-4528422a-d2db-4a7e-a9cd-4309285cc01f.png)


## Diff

<details>
  
<summary>前回との差分</summary>
  
[diff](https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/commit/2ff59c7b049c72800d85a65b4b9f48ad68b100b5)
  
</details>

## 最後に
今回はこれにて終了です。お疲れさまでした。

