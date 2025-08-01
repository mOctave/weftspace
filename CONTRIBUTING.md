# Contributing to Weftspace

Thank you for showing interest in my little project! I'd love your help in making this library more powerful and reliable.

## Issues

The [Issues](https://github.com/mOctave/weftspace/issues/) page is intended for posting bug reports and feature requests. When you post a new issue, please:
- Check to make sure it's not a duplicate of an existing issue
- Complete one issue per bug or feature you want to bring up
- Choose the proper template, and fill it out completely

If you're not sure whether something belongs in Issues or Discussions, don't worry too much about it. However, if it's isn't a bug report or feature request,
you should probably head over to [Discussions](https://github.com/mOctave/weftspace/discussions/) to post it instead.

## Pull Requests

Pull Requests are the most direct form of contributing to Weftspace. Feel free to PR any documentation fixes, code, or tests that you think would be helpful
for this project, so long as it follows the policies listed in this section.

When you PR your work, you relinquish sole ownership of it. Code—or anything else—you PR may be added, modified, or removed by any future contributor. If your
code is removed, you *will* still retain credit for your contribution to the file it was a part of.

Before you hurry to post your PR, please check the following:
- All tests are passing on your machine (use `mvn test`, `pytest tests/__init__.py`, etc.)
- The dif contains no accidental or unrelated changes
- All methods and fields have proper, helpful doc comments
- All indentation has been done with tabs, except for in XML files where two spaces should be used
- Lines wrap at a reasonable point (no hard limit, but please don't PR a 3000-char line of code)
- All other normal programming conventions are followed, and your work's style aligns roughly with the style of the existing codebase

If you feel like you've made significant changes to a code file, add your name to the copyright header of that file: `// Copyright (c) 2025 by mOctave, [yourNameHere]`.
If you're editing an old file and its been more than a year since it was created, update the date in the heading (`Copyright (c) 2025-[current year]`).

Please link any issues to your PR that you believe it addresses or are related to it.

Once you've posted a PR, I will review it (hopefully fairly quickly), and possibly ask you to tweak it. Once I am happy with the state of it, I will merge it into main,
and it is no longer your responsibility to worry about it (although I guess you can if you want to).
If I do not feel like your work fits with the project, I will try to tell you as soon as possible. I reserve the right to close any PR.

I will not personally review draft PRs, although I may comment on them and you're free to review whatever PRs you want.

## AI Policy

Personally, I am opposed to the use of generative AI, and there are a number of ethical and legal issues around its usage that I really don't want to have to worry about.
For that reason:
- Asking AI simple questions and using it to provide feedback on your work or link to external sources is acceptable (eg "Is this proper grammar?" or "Help me debug this code:")
- Using an AI like Grammarly to spot-check grammar is acceptable
- Using AI to generate, in whole or in part, code, documentation, or any other text or images for this project is **forbidden**

## Making Releases

*This section of the file is intended for contributors with write access to the repo who are making releases. If you do not have write access—which you probably don't—you can ignore this section for now.*

Before you publish a release, please check all the following:
- Changelog has been updated to reflect all changes and give appropriate credit
- Version number has been bumped in `java/pom.xml`, `python/weftspace/_version.py`, and `site/index.html`
- Release description mentions all contributors to the release
- All major or breaking changes are properly outlined in the release description
- The release is correctly categorized as a Stable, Unstable, Beta, or Test Release
- All checks are passing on the main branch

### Release Categories

**Stable:** Used to indicate a release that should almost definitely be bug-free. These releases should mostly consist of minor bugfixes, and any major changes should have been thoroughly tested first.

**Unstable:** Used to indicate a normal release. It still *shouldn't* have bugs in it, but some minor ones are probably going to show up anyways. Any release that contains lots of new features will probably be unstable.

**Beta:** Used to indicate old releases of the ESDF Manager public beta. It should not be used anymore. Mark releases as unstable instead.

**Test:** Used to indicate that a release is entirely for testing GitHub actions and deployment to various platforms and should not be used for production. Do not create discussions for test releases, and mark them as a pre-release. Be sure to uncheck the "mark as latest release" box.

If a release is marked as a test release, it is liable to be deleted from GitHub releases and yanked from PyPI without warning. Once everything is working, be sure to do so.
