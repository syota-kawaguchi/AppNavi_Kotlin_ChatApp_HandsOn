# アプリナビ Kotlin HandsOn

## 4.2 チャットのレイアウト作成

今回はチャットのレイアウトを作成し、`ChatLogActivity`に表示させます。

## チャットのレイアウトの作成

- まずdrawableファイルを作成します。`new` → `Drawable Resource File`で`chat_log_from_background`という名前でファイルを作成しましょう。
- 追加できましたら以下のように編集します。

```xml
    <item>
        <shape android:shape="rectangle">
            <corners android:radius="10dp"/>
            <solid android:color="@color/base_color_blue"/>
        </shape>
    </item>
```

- 続いてlayoutファイルを追加します。`new` → `layout Resource File`で`chat_from_row`という名前でファイルを作成しましょう。
- 追加できましたら`Palette`から`ImageView`を配置し、以下のように設定します。
  - `id` : `imageView_chat_log`
  - `layout_width` : `50dp`
  - `layout_height` : `50dp`
  - constraint
    - right : 画面右端
    - top : 画面上端
  - `margin`
    - `top` : `8dp`
    - `right` : `8dp`
  - ViewモードをCodeに変更し、`ImageView`を`de.hdodenhof.circleimageview.CircleImageView`に書き換え
- 次に`TextView`を配置します。
  - `id` : `textview_chat_log`
  - constraint
    - right : `imageView_chat_log`の左
    - top : `imageView_chat_log`の上
  - `margin right` : `8dp`
  - `background` : `@drawable/chat_log_from_background`
  - `maxWidth` : `240dp`
  - `padding` : 16dp
  - `text` : "This is my message that will wrap into multiple lines and keep on going"
  - `textColor` : `@color/white`
- 以下のような画面になりましたらOKです。

![session4 2-chat-from-row-layout](https://user-images.githubusercontent.com/57338033/157143270-8ce4d370-0de5-403d-a141-1a1f00108e18.png)

- `chat_to_row`の実装をしていきます。
- まず色を追加します。valuesフォルダーの`colors`ファイルを開き、以下の行を追加します。

```xml
    <color name="chat_to_row_background">#E7E6E9</color>
```

<!-- - drawableフォルダーの`chat_log_from_background`をコピペします。`res/drawable/chat_log_from_background.xml`の上で右クリックし、メニューで`Refactor` -> `Copy File...`をクリックします。ダイアログが表示されるので、"New name:"の欄に`chat_log_to_background.xml`と入力します。 -->
- 新たなdrawableファイルを作成します。`new` → `Drawable Resource File`で`chat_log_to_background`という名前でファイルを作成しましょう。
- 追加できましたら以下のように編集します。

```xml
  <?xml version="1.0" encoding="utf-8"?>
  <selector xmlns:android="http://schemas.android.com/apk/res/android">
      <item>
          <shape android:shape="rectangle">
              <corners android:radius="10dp"/>
              <solid android:color="@color/chat_to_row_background"/>
          </shape>
      </item>
  </selector>
```

- 新たなlayoutファイルを作成します。`new` → `Layout Resource File`で`chat_to_row`という名前でファイルを作成しましょう。
- 追加できましたら、"Design"から"Code"に切り替え、以下のように編集します。

```xml
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

![session4 2-chat-to-row-layout](https://user-images.githubusercontent.com/57338033/157144423-89899841-b14c-42fa-884c-81feb8cb6073.png)

- 続いてAdapterを実装します。
- Adapterを実装するにあたって`MessageAdapter`と重複する処理があるので、`New` → `Kotlin Class/File`から`AdapterUtil`というファイルを作成し、以下のように編集します。

```kotlin
  package com.example.handsonchatapp

  import android.view.View

  class AdapterUtil {
      interface ListListener<T> {
          fun onClickItem(tappedView: View, messageItem: T)
      }
  }
```

- `MessageAdapter`を以下のように編集します。

```diff
  package com.example.handsonchatapp

  import android.view.LayoutInflater
- import android.view.View
  import android.view.ViewGroup
  import androidx.recyclerview.widget.RecyclerView
  import com.example.handsonchatapp.databinding.MessageRowBinding
  import com.squareup.picasso.Picasso

- class MessageAdapter(private val messageItems: List<MessageItem>, private val listener : ListListener) : RecyclerView.Adapter<MessageViewHolder>() {
-
-   interface ListListener {
-       fun onClickItem(tappedView: View, messageItem: MessageItem)
-   }
+ class MessageAdapter(private val messageItems: List<MessageItem>, private val listener : AdapterUtil.ListListener<MessageItem>) : RecyclerView.Adapter<MessageViewHolder>() {

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
          val itemBinding = MessageRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
          return MessageViewHolder(itemBinding)
      }

      override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
          holder.bind(messageItems[position], listener)
      }

      override fun getItemCount(): Int = messageItems.size
  }

  class MessageViewHolder(private val itemBinding: MessageRowBinding) : RecyclerView.ViewHolder(itemBinding.root) {
-     fun bind(item: MessageItem, listener: MessageAdapter.ListListener) {
+     fun bind(item: MessageItem, listener: AdapterUtil.ListListener<MessageItem>) {
          itemBinding.usernameTextviewMessage.text = item.username
          itemBinding.latestmessageTextviewMessage.text = item.message
          val userImage = itemBinding.userimageImageviewMessage
          Picasso.get().load(item.progileImageUrl).into(userImage)
          itemBinding.root.setOnClickListener {
              listener.onClickItem(it, item)
          }
      }
  }

  class MessageItem(val username: String, val message: String, val progileImageUrl: String) {
      constructor() : this("", "", "")
  }
```

- `MessageActivity`からエラーが出ると思うので、赤のハイライト(エラー部分)を消し、緑のハイライトに修正しましょう

```diff
- object : MessageAdapter.ListListener {
+ object : AdapterUtil.ListListener<MessageItem> {
```

- ChatLogのAdapterを実装していきます。
- `New` → `Kotlin Class/File`から`ChatLogAdapter`という名前でファイルを作成し、以下のように編集します。

```kotlin
  package com.example.handsonchatapp

  import android.view.LayoutInflater
  import android.view.View
  import android.view.ViewGroup
  import android.widget.ImageView
  import android.widget.TextView
  import androidx.recyclerview.widget.RecyclerView
  import com.squareup.picasso.Picasso

  class ChatLogAdapter(private val list: List<ChatLogItem>, private val listener: AdapterUtil.ListListener<ChatLogItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

      override fun getItemViewType(position: Int): Int = if (list[position].isFrom) 0 else 1

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
          val layout : Int = if (viewType == 0) R.layout.chat_from_row else R.layout.chat_to_row
          val itemView: View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
          return ChatLogViewHolder(itemView, viewType)
      }

      override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
          holder.itemView.findViewById<TextView>(R.id.textview_chat_log).text = list[position].message
          val url = list[position].profileImageUrl
          val targetImageView = holder.itemView.findViewById<ImageView>(R.id.imageView_chat_log)
          if (url != "") {
              Picasso.get().load(url).into(targetImageView)
          }
          holder.itemView.setOnClickListener {
              listener.onClickItem(it, list[position])
          }
      }

      override fun getItemCount(): Int = list.size
  }

  class ChatLogViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
      val chatLog : TextView = itemView.findViewById<TextView>(R.id.textview_chat_log)
      val profileImageUrl : ImageView = itemView.findViewById<ImageView>(R.id.imageView_chat_log)
  }

  class ChatLogItem(val username: String, val message: String, val profileImageUrl: String, val isFrom: Boolean) {
      constructor() : this("", "", "", false)
  }
```

- ここまで入力できましたら`ChatLogActivity`を編集してダミーメッセージを表示させます。以下のように編集しましょう。

```kotlin
  package com.example.handsonchatapp

  import androidx.appcompat.app.AppCompatActivity
  import android.os.Bundle
  import android.view.View
  import androidx.recyclerview.widget.LinearLayoutManager
  import androidx.recyclerview.widget.RecyclerView
  import com.example.handsonchatapp.databinding.ActivityChatLogBinding

  class ChatLogActivity : AppCompatActivity() {

      private val TAG = "ChatLogActivity"

      private lateinit var binding : ActivityChatLogBinding

      private var recyclerView : RecyclerView? = null

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_chat_log)

          binding = ActivityChatLogBinding.inflate(layoutInflater)
          val view = binding.root
          setContentView(view)

          recyclerView = binding.recyclerviewChatlog

          val chatLogs = mutableListOf<ChatLogItem>()
          chatLogs.add(ChatLogItem("username", "Hello world", "", true))
          chatLogs.add(ChatLogItem("username", "Hello world", "", false))
          chatLogs.add(ChatLogItem("username", "Hello world", "", true))
          chatLogs.add(ChatLogItem("username", "Hello world", "", false))

          recyclerView?.apply {
              setHasFixedSize(true)
              layoutManager = LinearLayoutManager(context)
              adapter = ChatLogAdapter(
                  chatLogs,
                  object : AdapterUtil.ListListener<ChatLogItem> {
                      override fun onClickItem(tappedView: View, messageItem: ChatLogItem) {}
                  }
              )
          }
      }
  }
```

- ここまでできましたら実行してみましょう。以下のような画面になればOKです。

![session4 2-chat-log-result](https://user-images.githubusercontent.com/57338033/157149865-4beab181-4979-4142-8d89-167aac89aee9.png)




## Diff

<details>
  
<summary>前回との差分</summary>
  
[diff](https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/commit/8d8ca452f1f8c8b145794d43b168dfd60273b936)
  
</details>

## Next

[session4.3 チャット機能の実装](https://github.com/syota-kawaguchi/AppNavi_Kotlin_ChatApp_HandsOn/tree/session4.2)