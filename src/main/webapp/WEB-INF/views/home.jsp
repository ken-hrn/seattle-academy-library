
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=utf8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>ホーム｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>

<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul class="right-content">
                <li><a href="<%=request.getContextPath()%>/home" class="btn btn-info">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/" class="btn btn-danger">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <div class="content_head">
            <h1>Home</h1>
            <form action="<%=request.getContextPath()%>/searchBook" method="post" class="search_box">
                <input type="radio" name="searchCriteria" value="perfectMatching" id="perfectMatching" checked="checked">
                <label for="perfectMatching" style="width: 110px;">完全一致</label>
                <input type="radio" name="searchCriteria" value="partialMatching" id="partialMatching">
                <label for="partialMatching" style="width: 110px;">部分一致</label>
                <input type="text" class="search1" name="searchWord" placeholder="本の検索">
            </form>
        </div>
        <a href="<%=request.getContextPath()%>/addBook" class="btn btn-primary">書籍の追加</a>
        <a href="<%=request.getContextPath()%>/bulkRegist" class="btn btn-success">書籍の一括登録</a>
        <a href="<%=request.getContextPath()%>/lendingHistroy" class="btn btn-warning">貸出履歴一覧</a>
        <div class="content_body">
            <c:if test="${!empty resultMessage}">
                <div class="error_msg">${resultMessage}</div>
            </c:if>
            <div>
                <div class="booklist">
                    <c:choose>
                        <c:when test="${!empty bookList}">
                            <c:forEach var="bookInfo" items="${bookList}">
                                <div class="books">
                                    <form method="get" class="book_thumnail" action="<%=request.getContextPath()%>/details">
                                        <a href="javascript:void(0)" onclick="this.parentNode.submit();">
                                            <c:if test="${bookInfo.thumbnail == 'null'}">
                                                <img class="book_noimg" src="resources/img/noImg.png">
                                            </c:if>
                                            <c:if test="${bookInfo.thumbnail != 'null'}">
                                                <img class="book_noimg" src="${bookInfo.thumbnail}">
                                            </c:if>
                                        </a> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                                    </form>
                                    <ul>
                                        <li class="book_title">${bookInfo.title}</li>
                                        <li class="book_author">${bookInfo.author}</li>
                                        <li class="book_publisher">出版社：${bookInfo.publisher}</li>
                                        <li class="book_publisher">出版日：${bookInfo.publishDate}</li>
                                    </ul>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div>条件に一致する検索結果はありません。</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>
</body>
</html>
