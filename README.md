# Task Manager

A simple task manager written to demonstrate various new Java language features and APIs.

* uses [`var` to infer local variable types](https://blog.codefx.org/java/java-10-var-type-inference/)
* uses the improved process API to query all running processes for their information (see [package `task`](src/main/java/org/codefx/demo/task_manager/task))
* implements the new flow API to steer tasks from query to filter to presentation (see [package `flow`](src/main/java/org/codefx/demo/task_manager/flow) and [main class `TaskManager`](src/main/java/org/codefx/demo/task_manager/TaskManager.java))
