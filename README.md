# Space Invaders

Classic Space Invaders game recreated in JAVA. Control your spaceship to defend Earth by shooting down waves of alien invaders. Features retro-style graphics, dynamic gameplay, and increasing difficulty levels. Play and relive the arcade nostalgia!

# Contributing

## Setup

Navigate to your desired directory and run this command:

```bash
# clone the remote repository into your machine
git clone https://github.com/LOK-PLOK/Space-Invaders.git
# change current working directory to the project
cd Space-Invaders
# get the newest version of the remote repo
git pull
# open the current directory in your preferred text editor
code .
```

## Contribution Conventions

Here's an example commit flow with git:

```bash
# sync latest code from the remote repository
git pull
# create a new branch based on the feature you want to work on
git checkout -b <new_branch>
# after making some changes, add and commit your work
git add .
git commit -m "category: do something"
# push your changes and make a pull request on GitHub afterwards so that I can review them
git push origin HEAD
```

The syntax for making `git commit -m <insert_message_here>` messages should follow this syntax for consistency:

```bash
"category: do something"
```

1. `do something` must be written in imperative tone
2. `category` must fall under these categories;
   - `feat:` introduces a new feature or component to the codebase
   - `style:` changes a layout, stylesheet, UI look of a certain component
   - `fix:` patches a bug
   - `docs:` any addition pertaining to documentation (comments, README.md, etc)
   - `nit:` small change
   - `BREAKING CHANGE:` a change that dramatically changes a pre-existing system - possibly leading to bugs to be patched