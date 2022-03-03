# アプリナビ Kotlin HandsOn

## 1.1 入力フォームを作成する
これからユーザー登録画面を作成していきます。<br>
今後わかりやすいようにするため、まずはファイル名を変更します。<br>
- `MainActivity`上にカーソルをのせ、右クリックし`Refactor`→`Rename`を選択します。<br>
- `MainActivity`を`RegisterActivity`に変更し`Refactor`をクリックしましょう
- `res/layout/activity_main.xml`も同様に`activity_main`から`activity_register`に変更しましょう

![session1-1-rename-mainactivity](https://user-images.githubusercontent.com/57338033/156504019-db0913b0-2174-4c5d-adb2-9742651fc47e.png)

では、先程変更した`activity_register.xml`を開きましょう

![session1-1-helloworld](https://user-images.githubusercontent.com/57338033/156507290-de907a28-ed9f-444c-b81f-84324778ab59.png)

簡単にAndroidStudioの画面の説明をしておきます
- 左端のサイドバー<br>
プロジェクトのファイル・フォルダー構成
- Palette<br>
レイアウトにドラッグできるビュー及びビューグループが格納されている
- View mode<br>
  - Codeモード：UIをコードベースで編集するモード
  - Desingモード：UIを視覚的に編集するモード
  - Splitモード：CodeモードとDesignモードを両方表示するモード
 - Attributes <br>
 選択したUIパーツの詳細を設定する。

これからユーザー名・メールアドレス・パスワードの入力フォームを配置していきます。
 - PaletteからPlaneTextを画面にドラッグ＆ドロップします。
 
 ![session1-1-put-planetext](https://user-images.githubusercontent.com/57338033/156542377-ddd91435-1dce-4d6c-af42-188766603172.png)

 - Planeテキストの大きさを以下のように設定します。
 - 大きさは先程配置したPlaneTextを選択した状態で`Attributes`の`layout_width`, `layout_height`から変更できます。
 - 以下のように設定します。
 ```
 lauout_width : 0dp
 layout_height : 50dp
 ``` 
![session1-1-set-width-height](https://user-images.githubusercontent.com/57338033/156543586-dab474a8-104e-4bb0-94d3-b8f6b85a0c7b.png)

次に`Constraint`を設定します。
- 配置したPlaneTextを見ると上下左右に◯があります。左の◯は左の画面端に、右の◯は右の画面端に、上の◯は上の画面端までドラッグ＆ドロップしましょう(下の◯は設定しない)
- するとPlaneTextが横いっぱいに広がります。

![session1-1-Set-Constraint](https://user-images.githubusercontent.com/57338033/156557363-ed873e4f-1d22-4d1a-aa3d-18fb255b1891.png)

画面いっぱいに広がって見ずらいので余白を設定します。余白はmerginで設定します。
- `Attributes`の`Constraint Widget`から以下のようにmerginを設定します。
```
mergin left : 32dp
mergin right : 32dp
mergin top : 32dp
```

Hintを設定します。Hintは何も入力されていないときに表示されるメッセージです。このフォームにはどういったものが入力されるべきであるかを示します。
- `Attributes`の`Text`に「Name」と入力されていると思います。これを消します。
- `Attributes`の`Hint`に「ユーザー名」と入力しましょう
- すると画面のPlaneTextに「ユーザー名」と表示されると思います。

idを設定しましょう。これは後にコードとUIを関連付けるために使います。
- idを「username_edittext_register」とします。<br>

ここまでで一旦ユーザー名を入力するフォームの設定は終了です。下図のようになっていれば問題ないです。

![session1-1-done-set-username-form](https://user-images.githubusercontent.com/57338033/156560816-44de6c78-c313-416c-8c4a-32466da7c7f6.png)

次にメールアドレスのフォーム、パスワードのフォームを設定していきます。
- `Attributes`から`Email`、`Password`を画面にドラッグ＆ドロップ
- `Email`を以下のように設定しましょう
```
layout_width : 0dp
layout_height : 50dp

constrain left → 画面左端
constrain right →　画面右端
constrain Top → Username(先程のPlaneText)の下

mergin left : 32dp
mergin right : 32dp
mergin top : 16dp

Textを消し、Hintに「メールアドレス」と入力

idを「email_edittext_register」に変更
```

- 最後に`Password`を以下のように設定しましょう
```
layout_width : 0dp
layout_height : 50dp

constrain left → 画面左端
constrain right →　画面右端
constrain Top → Emailの下

mergin left : 32dp
mergin right : 32dp
mergin top : 16dp

Textを消し、Hintに「パスワード」と入力

idを「password_edittext_register」に変更
```
以下のような画面になっていればOKです。

![session1-1-result](https://user-images.githubusercontent.com/57338033/156562495-dbf88cce-1250-4cbc-8fd1-0210ae8f232c.png)

## Next


