#CONTRIBUTING

We welcome new contributors. Here you can read our guidelines to contribute to the IRIS project.

## Found a bug

If you found a bug in our software or mistakes in the documentation there are two ways to help us:

1. [Submit an issue](#submitting-an-issue) which explains the bug to our [GitHub Repository](https://github.com/Contargo/iris/issues/).
2. [Submit a pull request](#pull-requests) that fixes the bug.


## Want a new Feature

If you want a new Feature in IRIS there are two ways to get it done:

1. Request the new feature by [submitting an issue](#submitting-an-issue) to our [GitHub Repository](https://github.com/Contargo/iris/issues/).
2. [Submit a Pull Request](#pull-requests) that contains the new feature.


## Submission Guidelines

### Submitting an Issue

Before you submit an issue check the [GitHub Repository](https://github.com/Contargo/iris/issues/) to see if someone else reported the same issue.

If you submit a bug, provide as much information as needed for us to reproduce the bug.


### Pull Requests

Before submitting a pull request do following things:

* If you are not familiar with GitHubs pull requests take a look at their documentation of [Using pull requests](https://help.github.com/articles/using-pull-requests/).
* Search our [GitHub Repository](https://github.com/Contargo/iris) for pull requests doing the same, so you don't have to put effort in something that's already done.
* If there is no active pull request implementing your feature then fork [IRIS](https://github.com/Contargo/iris)
* Create a new branch:
```
    git checkout -b my-branch master
```
* Make your changes.
* Write Unit-Tests for all your changes.
* Run the full test suite to verify you did not broke anything:
```
    mvn clean verify
```
* Commit your changes. Write a commit message that explains what your changes are for, so everyone can understand what it does. See [this](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html) explanation for how to right commit message. If your changes are related to an open issue, reference the issue number in the last line of your commit message. Example: ```References #12```.
* Push the branch with your changes to GitHub. ```git push origin my-branch```
* In GitHub, send a pull request to ```iris:master```

If you want to learn more about, how to write a good pull request, read [this](https://github.com/blog/1943-how-to-write-the-perfect-pull-request) blog post of GitHub.
